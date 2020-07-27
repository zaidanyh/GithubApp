package com.dicoding.lastsubmission.ui

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.dicoding.lastsubmission.R
import com.dicoding.lastsubmission.adapter.ViewPagerAdapter
import com.dicoding.lastsubmission.db.DatabaseContract
import com.dicoding.lastsubmission.db.UserHelper
import com.dicoding.lastsubmission.helper.MappingHelper
import com.dicoding.lastsubmission.helper.RequestInterface
import com.dicoding.lastsubmission.helper.ServiceGenerator
import com.dicoding.lastsubmission.model.Items
import com.dicoding.lastsubmission.model.ResponseDetail
import com.dicoding.lastsubmission.model.User
import kotlinx.android.synthetic.main.activity_detail.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailActivity : AppCompatActivity() {
    private lateinit var userHelper: UserHelper
    private var user: User? = null
    private var listUser: Items? = null
    private lateinit var avatar: String
    private var statusFavorite: Boolean = false

    companion object {
        const val KEY_USER = "key_user"
        const val KEY_FAV = "key_fav"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        userHelper = UserHelper.instance(applicationContext)
        userHelper.open()

        supportActionBar?.elevation = 0f
        supportActionBar?.title = resources.getString(R.string.detailUser)

        val viewPager = ViewPagerAdapter(this, supportFragmentManager)

        listUser = intent.getParcelableExtra(KEY_USER)
        if (listUser != null) {
            loadDataById(listUser!!.login)
            if (listUser?.login == user?.login) {
                statusFavorite = user!!.status
                getDetailUser(listUser!!.login)
                viewPager.username = listUser!!.login
            } else {
                getDetailUser(listUser!!.login)
                viewPager.username = listUser!!.login
            }
        } else {
            user = intent.getParcelableExtra(KEY_FAV)
            statusFavorite = user!!.status
            getDetailUser(user!!.login)
            viewPager.username = user!!.login
        }

        viewPage.adapter = viewPager
        tabLayout.setupWithViewPager(viewPage)

        setStatusFavorite(statusFavorite)
        fab_favorite.setOnClickListener {
            statusFavorite = !statusFavorite
            if (statusFavorite) {
                user?.avatar_url = avatar
                user?.login = usernameDetail.text.toString()
                user?.name = fullnameDetail.text.toString()
                user?.status = statusFavorite
                insertDB(avatar, usernameDetail.text.toString(), fullnameDetail.text.toString(), statusFavorite)
                setStatusFavorite(statusFavorite)
            } else {
                if (listUser != null) {
                    loadDataById(listUser!!.login)
                } else {
                    loadDataById(user!!.login)
                }
                deleteFromDB(user!!.id)
            }
        }
        btn_back.setOnClickListener {
            finish()
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

    private fun getDetailUser(login: String) {
        progressBar.visibility = View.VISIBLE

        val apiInterface = ServiceGenerator.createContent(RequestInterface::class.java)
        val call: Call<ResponseDetail> = apiInterface.getDetailUser(login)
        call.enqueue(object : Callback<ResponseDetail> {
            override fun onResponse(call: Call<ResponseDetail>, response: Response<ResponseDetail>) {
                if (response.body() != null) {
                    avatar = response.body()?.avatar_url.toString()
                    Glide.with(this@DetailActivity)
                        .load(response.body()?.avatar_url)
                        .into(avatarDetail)
                    fullnameDetail.text = response.body()?.name
                    usernameDetail.text = response.body()?.login
                    if (response.body()!!.company == null) {
                        companyDetail.text = "-"
                    } else {
                        companyDetail.text = response.body()?.company
                    }
                    if (response.body()?.location == null) {
                        locationDetail.text = "-"
                    } else {
                        locationDetail.text = response.body()?.location
                    }
                    repository.text = response.body()?.public_repos.toString()
                    followers.text = response.body()?.followers.toString()
                    following.text = response.body()?.following.toString()
                }
                progressBar.visibility = View.INVISIBLE
            }

            override fun onFailure(call: Call<ResponseDetail>, t: Throwable) {
                Log.d("TAG", t.message.toString())
                progressBar.visibility = View.INVISIBLE
            }
        })
    }

    private fun setStatusFavorite(status: Boolean) {
        if (status) {
            fab_favorite.setImageResource(R.drawable.favorite)
        } else {
            fab_favorite.setImageResource(R.drawable.unfavorite)
        }
    }

    private fun loadDataById(username: String) {
        val load = userHelper.getByUsername(username)
        val resultLoad = MappingHelper.mappingCursorbyUsername(load)
        user = resultLoad
    }

    private fun insertDB(avatar: String, username: String, fullname: String, status: Boolean) {
        val values = ContentValues().apply {
            put(DatabaseContract.UserColumns.AVATAR, avatar)
            put(DatabaseContract.UserColumns.USERNAME, username)
            put(DatabaseContract.UserColumns.FULLNAME, fullname)
            put(DatabaseContract.UserColumns.STATUS, status)
        }

        val result = userHelper.insert(values)

        if (result > 0) {
            user?.id = result.toInt()
            Toast.makeText(this, resources.getString(R.string.addFav), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, resources.getString(R.string.addFavFail), Toast.LENGTH_SHORT).show()
        }
    }

    private fun deleteFromDB(id: Int) {
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle(resources.getString(R.string.deleteTitle))
        alertDialog
            .setMessage(resources.getString(R.string.deleteMessage))
            .setCancelable(false)
            .setPositiveButton(resources.getString(R.string.buttonPositive)) { _, _ ->
                val result = userHelper.delete(id.toString()).toLong()
                if (result > 0) {
                    Toast.makeText(this, resources.getText(R.string.delete), Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, FavoriteActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this, resources.getString(R.string.deleteFail), Toast.LENGTH_SHORT).show()
                }
                setStatusFavorite(statusFavorite)
            }
            .setNegativeButton(resources.getString(R.string.buttonNegative)) { dialog, _ ->
                statusFavorite = ! statusFavorite
                setStatusFavorite(statusFavorite)
                dialog.cancel()
            }
        val alert = alertDialog.create()
        alert.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        userHelper.close()
    }
}