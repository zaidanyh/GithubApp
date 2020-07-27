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
import kotlinx.android.synthetic.main.fragment_follower.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class FollowerFragment : Fragment() {
    val resultFollow = MutableLiveData<ArrayList<ListFollow>>()

    companion object {
        private const val USERNAME = "username"

        fun newIntance(login: String): FollowerFragment {
            val fragment = FollowerFragment()
            val bundle = Bundle()
            bundle.putString(USERNAME, login)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_follower, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val username = arguments?.getString(USERNAME)
        getFollowers(username.toString())

        resultFollow.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                val adapter = ListFollowAdapter(it)
                adapter.notifyDataSetChanged()
                rv_follower.layoutManager = LinearLayoutManager(context)
                rv_follower.adapter = adapter
            }
        })
    }

    private fun getFollowers(login: String) {
        val reqInterface = ServiceGenerator.createContent(RequestInterface::class.java)
        val call: Call<ArrayList<ListFollow>> = reqInterface.getFollowersUser(login)
        call.enqueue(object: Callback<ArrayList<ListFollow>> {
            override fun onFailure(call: Call<ArrayList<ListFollow>>, t: Throwable) {
                Log.d("FollowersFrag", t.message.toString())
            }

            override fun onResponse(call: Call<ArrayList<ListFollow>>, response: Response<ArrayList<ListFollow>>) {
                if (response.isSuccessful) {
                    resultFollow.postValue(response.body())
                }
            }
        })
    }
}