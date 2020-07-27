package com.dicoding.lastsubmission.ui

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.lastsubmission.R
import com.dicoding.lastsubmission.adapter.ListUserAdapter
import com.dicoding.lastsubmission.helper.*
import com.dicoding.lastsubmission.model.Items
import com.dicoding.lastsubmission.model.ResponseDataUser
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {
    val resultSearch = MutableLiveData<ArrayList<Items>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar!!.title = resources.getString(R.string.title)

        val adapter = ListUserAdapter()
        adapter.notifyDataSetChanged()
        rv_list_user.layoutManager = LinearLayoutManager(this)
        rv_list_user.adapter = adapter

        resultSearch.observe(this, Observer {
            if (it != null) {
                adapter.setData(it)
                progressBar.visibility = View.INVISIBLE
                adapter.setOnClickListener(object : ListUserAdapter.OnClickListener {
                    override fun onItemCLicked(items: Items) {
                        val intent = Intent(this@MainActivity, DetailActivity::class.java)
                        intent.putExtra(DetailActivity.KEY_USER, items)
                        startActivity(intent)
                    }
                })
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                progressBar.visibility = View.VISIBLE
                getUserGithub(query)
                starter.visibility = View.INVISIBLE
                rv_list_user.visibility = View.VISIBLE
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.settings -> startActivity(Intent(this, SettingActivity::class.java))
            R.id.favorite -> startActivity(Intent(this, FavoriteActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getUserGithub(q: String) {

        val apiInterface: RequestInterface = ServiceGenerator.createContent(RequestInterface::class.java)
        val call: Call<ResponseDataUser> = apiInterface.getUsers(q)
        call.enqueue(object: Callback<ResponseDataUser>{
            override fun onResponse(call: Call<ResponseDataUser>, response: Response<ResponseDataUser>) {
                if (response.isSuccessful) {
                    resultSearch.postValue(response.body()!!.items)
                }
            }

            override fun onFailure(call: Call<ResponseDataUser>, t: Throwable) {
                Log.d("TAG", t.message.toString())
                progressBar.visibility = View.INVISIBLE
            }
        })
    }
}