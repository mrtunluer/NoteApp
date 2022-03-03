package com.yks.noteapp.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yks.noteapp.R
import com.yks.noteapp.adapter.NotesAdapter
import com.yks.noteapp.databinding.FragmentNotesBinding
import com.yks.noteapp.model.Note
import com.yks.noteapp.ui.viewmodel.NoteViewModel
import com.yks.noteapp.utils.SwipeGesture
import com.yks.noteapp.utils.ViewState
import com.yks.noteapp.utils.snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect


@AndroidEntryPoint
class NotesFragment : Fragment() {

    private var _binding: FragmentNotesBinding? = null
    private val binding get() = _binding!!
    private val viewModel: NoteViewModel by viewModels()
    private lateinit var notesAdapter: NotesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotesBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getAllNotes()
        initAdapter()
        swipeToDelete()
        observeNotes()
        clickNote()

        binding.addBtn.setOnClickListener {
            val bundle = bundleOf("note" to null)
            findNavController().navigate(R.id.action_notesFragment_to_addEditFragment,bundle)
        }

    }

    private fun initAdapter() {
        notesAdapter = NotesAdapter()
        binding.recyclerView.apply {
            adapter = notesAdapter
            layoutManager = LinearLayoutManager(requireActivity())
        }
    }

    private fun observeNotes() {
        lifecycleScope.launchWhenStarted {
            viewModel.state.collect { uiState ->
                when (uiState){
                    is ViewState.Loading -> binding.progress.visibility = View.VISIBLE
                    is ViewState.Empty -> showEmpty()
                    is ViewState.Success -> {
                        binding.progress.visibility = View.GONE
                        loaded(uiState.notes)
                    }
                    is ViewState.Error -> {
                        binding.progress.visibility = View.GONE
                        requireView().snackbar("Error")
                    }
                }
            }
        }
    }

    private fun swipeToDelete(){
        val swipeGesture = object : SwipeGesture(){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val note = notesAdapter.getItem(position)
                viewModel.deleteNote(note.id)
            }
        }
        ItemTouchHelper(swipeGesture).apply {
            attachToRecyclerView(binding.recyclerView)
        }
    }

    private fun clickNote() {
        notesAdapter.setOnItemClickListener {
            val bundle = bundleOf("note" to it)
            findNavController().navigate(R.id.action_notesFragment_to_addEditFragment,bundle)
        }

    }

    private fun showEmpty() {
        binding.emptyLayout.emptyLayout.visibility = View.VISIBLE
        notesAdapter.submitList(emptyList())
    }

    private fun loaded(notes: List<Note>) {
        binding.emptyLayout.emptyLayout.visibility = View.GONE
        notesAdapter.submitList(notes)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
