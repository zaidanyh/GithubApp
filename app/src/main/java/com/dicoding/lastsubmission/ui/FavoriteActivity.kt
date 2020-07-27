package com.dicoding.lastsubmission.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.lastsubmission.R
import com.dicoding.lastsubmission.adapter.FavoriteAdapter
import com.dicoding.lastsubmission.db.UserHelper
import com.dicoding.lastsubmission.helper.MappingHelper
import com.dicoding.lastsubmission.model.User
import kotlinx.android.synthetic.main.activity_favorite.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavoriteActivity : AppCompatActivity() {
    private lateinit var userAdapter: FavoriteAdapter
    private lateinit var userHelper: UserHelper

    companion object {
        private const val EXTRA_SAVE = "extra_save"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)
        supportActionBar?.title = resources.getString(R.string.myFavorite)
        progressBarFav.visibility = View.VISIBLE

        rv_favorite.layoutManager = LinearLayoutManager(this)
        rv_favorite.hasFixedSize()
        userAdapter = FavoriteAdapter()
        rv_favorite.adapter = userAdapter
        userAdapter.setOnClickListener(object: FavoriteAdapter.OnItemClickListener {
            override fun onItemClicked(user: User) {
                val intent = Intent(this@FavoriteActivity, DetailActivity::class.java)
                intent.putExtra(DetailActivity.KEY_FAV, user)
                startActivity(intent)
                finish()
            }
        })

        userHelper = UserHelper.instance(applicationContext)
        userHelper.open()

        if (savedInstanceState == null) {
            loadDatafromDatabase()
        } else {
            val fav = savedInstanceState.getParcelableArrayList<User>(EXTRA_SAVE)
            if (fav != null) {
                userAdapter.listFavorite = fav
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.my_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.settings -> startActivity(Intent(this, SettingActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_SAVE, userAdapter.listFavorite)
    }

    private fun loadDatafromDatabase() {
        GlobalScope.launch(Dispatchers.Main) {
            val deferredUser = async(Dispatchers.IO) {
                val loadUser = userHelper.getAll()
                MappingHelper.mappingCursor(loadUser)
            }
            progressBarFav.visibility = View.INVISIBLE
            val fav = deferredUser.await()
            if (fav.size > 0) {
                userAdapter.listFavorite = fav
            } else {
                userAdapter.listFavorite = ArrayList()
                Toast.makeText(this@FavoriteActivity, resources.getString(R.string.loadList), Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        userHelper.close()
    }
}