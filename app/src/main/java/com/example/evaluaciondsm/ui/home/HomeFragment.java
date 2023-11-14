package com.example.evaluaciondsm.ui.home;

import android.app.admin.DeviceAdminInfo;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.evaluaciondsm.AdminSQLiteOpenHelper;
import com.example.evaluaciondsm.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private EditText txt_code, txt_description, txt_price;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save();
            }
        });

        binding.btnSearchP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search();
            }
        });

        binding.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delete();
            }
        });

        binding.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit();
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    //Metodo para guardar los productos
    public void save() {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(getContext(), "products", null, 1);
        SQLiteDatabase db = admin.getWritableDatabase();

        String code = binding.txtCode.getText().toString();
        String name = binding.txtDescription.getText().toString();
        String price = binding.txtPrice.getText().toString();

        if (!code.isEmpty() && !name.isEmpty() && !price.isEmpty()) {
            ContentValues register = new ContentValues();
            register.put("code", code);
            register.put("name", name);
            register.put("price", price);

            db.insert("products", null, register);
            db.close();

            cleanForm();

            Toast.makeText(getContext(), "Registro exitoso", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Debes llenar todos los campos", Toast.LENGTH_SHORT).show();
        }

    }

    //Metodo para buscar productos
    public void search() {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(getContext(), "products", null, 1);
        SQLiteDatabase db = admin.getWritableDatabase();

        String code = binding.txtCode.getText().toString();

        if (!code.isEmpty()) {
            Cursor row = db.rawQuery("select name, price from products where code=" + code, null);

            if (row.moveToFirst()) {
                binding.txtDescription.setText(row.getString(0));
                binding.txtPrice.setText(row.getString(1));
                db.close();
            } else {
                Toast.makeText(getContext(), "No existe el producto", Toast.LENGTH_SHORT).show();
                db.close();
            }
        } else {
            Toast.makeText(getContext(), "Debes introducir el código del producto", Toast.LENGTH_SHORT).show();
        }
    }

    //metodo para eliminar un producto
    public void delete() {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(getContext(), "products", null, 1);
        SQLiteDatabase db = admin.getWritableDatabase();

        String code = binding.txtCode.getText().toString();

        if (!code.isEmpty()) {
            int cant = db.delete("products", "code=" + code, null);
            db.close();

            cleanForm();

            if (cant == 1) {
                Toast.makeText(getContext(), "Producto eliminado", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "El producto no existe", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getContext(), "Debes introducir el código del producto", Toast.LENGTH_SHORT).show();
        }
    }

    public void edit() {
        AdminSQLiteOpenHelper adminSQLiteOpenHelper = new AdminSQLiteOpenHelper(getContext(), "products", null, 1);
        SQLiteDatabase db = adminSQLiteOpenHelper.getWritableDatabase();
        String code = binding.txtCode.getText().toString();
        String name = binding.txtDescription.getText().toString();
        String price = binding.txtPrice.getText().toString();

        if (!code.isEmpty() && !name.isEmpty() && !price.isEmpty()) {
            ContentValues register = new ContentValues();
            register.put("code", code);
            register.put("name", name);
            register.put("price", price);

            int cant = db.update("products", register, "code=" + code, null);
            db.close();

            if (cant == 1) {
                Toast.makeText(getContext(), "Producto modificado", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "El producto no existe", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getContext(), "Debes llenar todos los campos", Toast.LENGTH_SHORT).show();
        }
    }

    public void cleanForm() {
        binding.txtCode.setText("");
        binding.txtDescription.setText("");
        binding.txtPrice.setText("");
    }



}