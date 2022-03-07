package com.yks.noteapp.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.yks.noteapp.R
import com.yks.noteapp.databinding.FragmentAddEditBinding
import com.yks.noteapp.model.Note
import com.yks.noteapp.ui.viewmodel.NoteViewModel
import com.yks.noteapp.utils.snackbar
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AddEditFragment : Fragment() {

    private var _binding: FragmentAddEditBinding? = null
    private val binding get() = _binding!!
    private val viewModel: NoteViewModel by viewModels()
    private val args: NotesFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddEditBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val note = args.note
        note?.let {
            getData(it)
        }

        binding.saveBtn.setOnClickListener {
            val title = binding.titleTxt.text.toString()
            val description = binding.descTxt.text.toString()
            val time = System.currentTimeMillis()
            note?.let {
                val id = note.id
                updateDb(id,title,description,time)
            }?:saveDb(title,description,time)
        }

        binding.backImg.setOnClickListener {
            it.findNavController().popBackStack()
        }

    }

    private fun getData(note: Note){
        binding.titleTxt.setText(note.title)
        binding.descTxt.setText(note.description)
    }

    private fun updateDb(id: Int, title: String, description: String, time: Long){
        when {
            description.isEmpty() -> {
                requireView().snackbar(getString(R.string.empty_desc))
            }
            title.isEmpty() -> {
                requireView().snackbar(getString(R.string.empty_title))
            }
            else -> {
                viewModel.updateNote(id, title, description, time).also {
                    requireView().snackbar(getString(R.string.note_saved)).also {
                        findNavController().navigate(R.id.action_addEditFragment_to_notesFragment)
                    }
                }
            }
        }
    }

    private fun saveDb(title: String, description: String, time: Long){
        when {
            description.isEmpty() -> {
                requireView().snackbar(getString(R.string.empty_desc))
            }
            title.isEmpty() -> {
                requireView().snackbar(getString(R.string.empty_title))
            }
            else -> {
                viewModel.insertNote(title, description, time).also {
                    requireView().snackbar(getString(R.string.note_saved)).also {
                        findNavController().navigate(R.id.action_addEditFragment_to_notesFragment)
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}