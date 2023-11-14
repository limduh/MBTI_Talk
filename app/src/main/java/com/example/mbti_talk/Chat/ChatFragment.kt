package nb_.mbti_talk.Chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import nb_.mbti_talk.databinding.FragmentChatBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ChatFragment : Fragment() {
    lateinit var binding: FragmentChatBinding
    lateinit var firebaseDatabase: DatabaseReference
    lateinit var recycler_chatroom: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatBinding.inflate(inflater, container, false)
        val view = binding.root

        // Firebase 및 RecyclerView 초기화
        firebaseDatabase = FirebaseDatabase.getInstance().getReference("ChatRoom")
        recycler_chatroom = binding.recyclerChatrooms
        recycler_chatroom.layoutManager = LinearLayoutManager(requireContext())

        val adapter = RecyclerChatRoomsAdapter(requireContext(), binding)
        recycler_chatroom.adapter = adapter

        return view
    }
}
