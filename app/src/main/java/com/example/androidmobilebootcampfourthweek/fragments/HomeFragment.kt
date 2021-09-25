package com.example.androidmobilebootcampfourthweek.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.input.input
import com.example.androidmobilebootcampfourthweek.MainActivity
import com.example.androidmobilebootcampfourthweek.R
import com.example.androidmobilebootcampfourthweek.base.BaseCallback
import com.example.androidmobilebootcampfourthweek.fragments.adapter.TodosAdapter
import com.example.androidmobilebootcampfourthweek.responseModels.GetResponse
import com.example.androidmobilebootcampfourthweek.responseModels.Todo
import com.example.androidmobilebootcampfourthweek.service.ServiceConnector
import com.example.androidmobilebootcampfourthweek.utils.changeStatusBarColor
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.item_todo.*
import kotlinx.android.synthetic.main.layout_dialog.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

class HomeFragment : Fragment(), TodosAdapter.OnClickListener {

    private var todoList = ArrayList<Todo>()
    private lateinit var todosAdapter : TodosAdapter

    val limit = 10
    var skip = 0
    var scrollLimit: Int = 1
    var scrollCount: Int = 0
    var taskCount: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        changeStatusBarColor(R.color.purple_700)

//        getTaskCount()

        getDataByPagination(limit, skip)

        return inflater.inflate(R.layout.fragment_home, container, false)

    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        when (taskCount){
//            0 -> Log.d("task:", "0")
//            else -> { getDataByPagination(limit, skip)
//                scrollLimit = taskCount/limit }
//        }

        floatingActionButton.setOnClickListener {
            showMaterialDialog()
        }



        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1) && newState==RecyclerView.SCROLL_STATE_IDLE && scrollCount <= scrollLimit) {
                    skip += limit
                    scrollCount++
                    progressBar.visibility = View.VISIBLE
                    getDataByPagination(limit, skip)

                    //region Optional Toast
                    // Toast.makeText( requireContext(), "Reached the end of page: $scrollCount", Toast.LENGTH_SHORT).show()
                    //endregion
                }
            }
        })

    }

    override fun onResume() {
        super.onResume()

        // in this block what we're simply doing is that we show a dialog
        // when the user presses the back button when in this fragment

        val activity = activity as MainActivity

        activity.onBackPressedDispatcher.addCallback(viewLifecycleOwner, object :
            OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                activity.finish()
            }
        })

    }


    /*  private fun getTaskCount() {
        ServiceConnector.restInterface.getAllTasks().enqueue(object : BaseCallback<GetResponse>() {

            override fun onSuccess(getResponse: GetResponse) {
                super.onSuccess(getResponse)

                taskCount = getResponse.count

            }

            override fun onFailure() {
                super.onFailure()
                noTaskMessage.visibility = View.VISIBLE
                Toast.makeText(
                    requireActivity(),
                    "Something went wrong, no tasks have been fetched",
                    Toast.LENGTH_SHORT
                ).show()
            }

        })
    }
    */


    private fun getDataByPagination(limit: Int, skip: Int){

        ServiceConnector.restInterface.getTaskByPagination(limit, skip).enqueue(object: BaseCallback<GetResponse>(){
            override fun onSuccess(getResponse: GetResponse) {
                super.onSuccess(getResponse)

                progressBar.visibility = View.GONE

                todoList.addAll(getResponse.data)

                if(todoList.size == 0){
                    noTaskMessage.visibility = View.VISIBLE
                }

//                todoList.reverse() // Optional

                todosAdapter = TodosAdapter(todoList, this@HomeFragment)

                recyclerView.adapter = todosAdapter

                recyclerView.layoutManager = LinearLayoutManager(requireContext())

            }

            override fun onFailure() {
                super.onFailure()
                noTaskMessage.visibility = View.VISIBLE
                Toast.makeText(requireActivity(), "Something went wrong, no tasks have been fetched", Toast.LENGTH_SHORT).show()
            }

        })
    }

    @SuppressLint("CheckResult")
    private fun showMaterialDialog(){

        val newTodo = Todo()

        MaterialDialog(requireActivity())
//            .customView(R.layout.layout_dialog) // Optional
            .show {
                title(R.string.add_new_task)
                input { _, newTaskDesc ->
                    newTodo.description = newTaskDesc.toString()
                    newTodo.completed = false
                }
            }
            .positiveButton(R.string.add){

//                todoList.add(newTodo)
//                todosAdapter.notifyItemInserted(todoList.size)

                ServiceConnector.restInterface.addTask(newTodo).enqueue(object: BaseCallback<Unit>(){
                    override fun onSuccess(data: Unit) {
                        Toast.makeText(requireActivity(), "Successfully added new task", Toast.LENGTH_SHORT).show()

                        getDataByPagination(limit, skip)

                    }

                    override fun onFailure(call: Call<Unit>, t: Throwable) {
                        Toast.makeText(requireActivity(), "Something went wrong", Toast.LENGTH_SHORT).show()
                        Log.e("failure", "on triggered")
                    }
                })

                Log.d("object", newTodo.description + " " + newTodo.completed.toString())
            }
            .negativeButton(R.string.dismiss)


        //region Optional Material Dialog

//            {
//                input { dialog, text ->
//                    newTodoText = text.toString()
//                }
//                positiveButton(R.string.add)
//                negativeButton(R.string.dismiss)
//            }
//
//        dialogButtonApprove.setOnClickListener{
//
//            Toast.makeText(requireContext(), "approved", Toast.LENGTH_SHORT).show()
//
//            val newTodo = Todo()
//            newTodo.description = editText.text.toString()
//            newTodo.completed = false
//
//            ServiceConnector.restInterface.addTask(newTodo).enqueue(object: Callback<Unit>{
//                override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
//
//                    Toast.makeText(requireActivity(), "Successfully added new task", Toast.LENGTH_SHORT).show()
//
//                }
//
//                override fun onFailure(call: Call<Unit>, t: Throwable) {
//                    Toast.makeText(requireActivity(), "Successfully went wrong", Toast.LENGTH_SHORT).show()
//                    Log.e("failure", "on triggered")
//                }
//            })
//        }
//
//        dialogButtonDecline.setOnClickListener{
//            dialog.dismiss()
//        }

        //endregion


    }


    data class CompleteBody(
        var completed: Boolean
    )


    override fun onCompleteButtonClick(position: Int) {

//        Toast.makeText(requireContext(), "Complete button clicked", Toast.LENGTH_SHORT).show() //Optional

        val clickedItem = todoList[position]

        clickedItem.completed = !clickedItem.completed

        todosAdapter.notifyItemChanged(position)

        val completeBody = CompleteBody(clickedItem.completed)

        ServiceConnector.restInterface.updateTaskById(clickedItem._id!!, completeBody).enqueue(object: BaseCallback<Unit>(){

            override fun onSuccess(data: Unit) {
                super.onSuccess(data)
                if(clickedItem.completed)
                    Toast.makeText(requireContext(), "Task ${position + 1} is completed", Toast.LENGTH_SHORT).show()
                else if (!clickedItem.completed){
                    Toast.makeText(requireContext(), "Task ${position + 1} is uncompleted", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure() {
                super.onFailure()

                Toast.makeText(requireContext(), "Failed to complete the task", Toast.LENGTH_SHORT).show()
            }

        })

    }



    override fun onDeleteButtonClick(position: Int) {

//        Toast.makeText(requireContext(), "Delete button clicked", Toast.LENGTH_SHORT).show() //Optional

        val clickedItem = todoList[position]

        ServiceConnector.restInterface.deleteTaskById(clickedItem._id!!).enqueue(object: BaseCallback<Unit>(){

            override fun onSuccess(data: Unit) {
                super.onSuccess(data)

                todoList.removeAt(position)

                todosAdapter.notifyItemRemoved(position)

                Toast.makeText(requireContext(), "Task ${position + 1} is deleted", Toast.LENGTH_SHORT).show()

            }

            override fun onFailure() {
                super.onFailure()
                Toast.makeText(requireContext(), "Failed to delete the task", Toast.LENGTH_SHORT).show()
            }


        })
    }

    private fun scrollToLastPosition(){
        recyclerView.scrollToPosition(todoList.size - 1)
    }


}





