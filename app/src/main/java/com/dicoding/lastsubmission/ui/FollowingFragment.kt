package com.dicoding.lastsubmission.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.lastsubmission.R
import com.dicoding.lastsubmission.adapter.ListFollowAdapter
import com.dicoding.lastsubmission.helper.RequestInterface
import com.dicoding.lastsubmission.helper.ServiceGenerator
import com.dicoding.lastsubmission.model.ListFollow
import kotlinx.android.synthetic.main.fragment_following.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowingFragment : Fragment() {
    val resultFollow = MutableLiveData<ArrayList<ListFollow>>()

    companion object {
        private const val USERNAME = "username"

        fun newInstance(login: String): FollowingFragment {
            val fragment = FollowingFragment()
            val bundle = Bundle()
            bundle.putString(USERNAME, login)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_following, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val username = arguments?.getString(USERNAME)
        getFollowing(username.toString())

        resultFollow.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                val adapter = ListFollowAdapter(it)
                adapter.notifyDataSetChanged()
                rv_following.layoutManager = LinearLayoutManager(context)
                rv_following.adapter = adapter
            }
        })
    }

    private fun getFollowing(login: String) {
        val reqInterface = ServiceGenerator.createContent(RequestInterface::class.java)
        val call: Call<ArrayList<ListFollow>> = reqInterface.getFollowingUser(login)
        call.enqueue(object : Callback<ArrayList<ListFollow>> {
            override fun onFailure(call: Call<ArrayList<ListFollow>>, t: Throwable) {
                Log.d("FollowingFrag", t.message.toString())
            }

            override fun onResponse(call: Call<ArrayList<ListFollow>>, response: Response<ArrayList<ListFollow>>) {
                if (response.isSuccessful) {
                    resultFollow.postValue(response.body())
                }
            }
        })
    }
}