package com.example.crud.produtossqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper
{

    // Versão da Base de Dados
    private static final int DATABASE_VERSION = 1;

    // Nome da Base de Dados
    private static final String DATABASE_NAME = "GestProdutos";

    // Nome da Tabela Produto
    private static final String TABLE_Produtos = "produtos";

    // Nome de colunas da tabela produtos
    private static final String KEY_ID_produto = "id";
    private static final String KEY_NOME_produto = "nome";
    private static final String KEY_Preco_produto = "preco";
    private static final String KEY_Desc_produto = "descricao";
    //Fazer o mesmo para outras tabelas

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_Produtos = "CREATE TABLE " + TABLE_Produtos + "("
                + KEY_ID_produto + " INTEGER PRIMARY KEY," + KEY_NOME_produto + " TEXT,"
                + KEY_Preco_produto + " REAL," + KEY_Desc_produto+ " TEXT " +")";
        db.execSQL(CREATE_TABLE_Produtos);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // Apaga a tabela se existir
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Produtos);

        // Cria novamente
        onCreate(db);
    }


    // Adicionar um produto
    public long addPruduto(Produto p) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NOME_produto, p.getNome()); // Nome do Produto
        values.put(KEY_Preco_produto, p.getPreco()); // Preço do Produto
        values.put(KEY_Desc_produto, p.getDesc()); // Descrição do Produto

        // Inserting Row
        long id = db.insert(TABLE_Produtos, null, values);
        db.close(); // Closing database connection

        return id;
    }


    // Obter um produto
    public Produto getProduto(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_Produtos, new String[]{KEY_ID_produto,
                        KEY_NOME_produto, KEY_Preco_produto,KEY_Desc_produto}, KEY_ID_produto + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        int id_=Integer.parseInt(cursor.getString(0));
        String nome_=cursor.getString(1);
        Double preco_ = cursor.getDouble(2);
        String desc_= cursor.getString(3);


        Produto p = new Produto(id_,nome_, preco_,desc_);
        // returnar o produto obtido
        return p;
    }


    //Obter todos os Produtos
    public List<Produto> getAllProdutos() {
        List<Produto> ListaProduto = new ArrayList<Produto>();

        // Selecionar todos
        String selectQuery = "SELECT  * FROM " + TABLE_Produtos;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // Percorrer a lista toda
        if (cursor.moveToFirst()) {
            do {
                Produto p = new Produto();
                p.setId(Integer.parseInt(cursor.getString(0)));
                p.setNome(cursor.getString(1));
                p.setPreco(cursor.getDouble(2));

                // Adicionar produto a lista de produtos
                ListaProduto.add(p);
            } while (cursor.moveToNext());
        }

        // return contact list
        return ListaProduto;
    }


    //Saber quantos produtos tem a lista
    public int getProdutosCount() {
        String countQuery = "SELECT  * FROM " + TABLE_Produtos;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }


    // Alterar um produto
    public int updateProduto(Produto p) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NOME_produto, p.getNome());
        values.put(KEY_Preco_produto, p.getPreco());

        // updating row
        return db.update(TABLE_Produtos, values, KEY_ID_produto + " = ?",
                new String[]{String.valueOf(p.getId())});
    }


    //Apagar um produto
    public void deleteProduto(String p) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_Produtos, KEY_ID_produto + " = ?", new String[]{p});
        db.close();
        //updateProduto(p);
    }



}
