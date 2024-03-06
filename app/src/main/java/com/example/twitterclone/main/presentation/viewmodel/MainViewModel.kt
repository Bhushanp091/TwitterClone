package com.example.twitterclone.main.presentation.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.twitterclone.auth.model.UserModel
import com.example.twitterclone.main.domain.Comment
import com.example.twitterclone.main.domain.Post
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val database: FirebaseDatabase,
    private val storage: FirebaseStorage
) : ViewModel() {


    val imageRef = storage.reference.child("posts/${UUID.randomUUID()}.jpg")
    val commentRef = storage.reference.child("comments/${UUID.randomUUID()}.jpg")
    val dataref = database.getReference("posts")
    val commentref = database.getReference("comments")

    private val _isTweetPosted = MutableLiveData<Boolean>()
    val isTweetPosted: LiveData<Boolean> = _isTweetPosted

    private val _isCommentPosted = MutableLiveData<Boolean>()
    val isCommentPosted: LiveData<Boolean> = _isCommentPosted

    private val _postsAndData = MutableLiveData<List<Pair<Post, UserModel>>>()
    val postsAndData: LiveData<List<Pair<Post, UserModel>>> = _postsAndData

    private val _commentsAndData = MutableLiveData<List<Pair<Comment, UserModel>>>()
    val commentsAndData: LiveData<List<Pair<Comment, UserModel>>> = _commentsAndData



    init {
        getPostsAndUserData() {
            _postsAndData.value = it.sortedByDescending {
                it.first.timeStamp
            }
        }

        getCommentsAndUserData {
            _commentsAndData.value = it.sortedByDescending {
                it.first.timeStamp
            }
        }
    }


    fun savePostImageData(
        description: String,
        imageUrl: Uri?,
        uid: String?,
    ) {

        val taskResult = imageUrl?.let { imageRef.putFile(it) }

        taskResult?.addOnSuccessListener {
            imageRef.downloadUrl.addOnSuccessListener {
                saveTweetData(description, it.toString(), uid)
            }
        }
    }

    fun saveTweetData(
        description: String,
        imageUri: String,
        uid: String?,
    ) {
        val timeStamp = System.currentTimeMillis().toString()
        val postId = dataref.push().key!!
        val tweetData = Post(description, imageUri, uid!!, postId, timeStamp)
        database.getReference("posts").child(postId).setValue(tweetData)
            .addOnSuccessListener {
                _isTweetPosted.value = true

            }.addOnFailureListener {
                _isTweetPosted.value = false
            }

    }


    private fun getPostsAndUserData(onResult: (List<Pair<Post, UserModel>>) -> Unit) {
        database.getReference("posts").addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                val result = mutableListOf<Pair<Post, UserModel>>()
                for (postsnapShot in snapshot.children) {
                    val posts = postsnapShot.getValue(Post::class.java)
                    posts?.let {
                        fetchUserDataFromPost(it) { user ->
                            result.add(0, it to user)
                            if (result.size == snapshot.childrenCount.toInt()) {
                                onResult(result)
                            }
                        }
                    }
                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    fun fetchUserDataFromPost(post: Post, onResult: (UserModel) -> Unit) {

        database.getReference("user").child(post.userId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val userData = snapshot.getValue(UserModel::class.java)
                    userData?.let(onResult)
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }


    fun saveCommentImageData(
        comment: String,
        imageUrl: Uri?,
        uid: String?,
        postId: String
    ) {

        val taskResult = imageUrl?.let { commentRef.putFile(it) }

        taskResult?.addOnSuccessListener {
            commentRef.downloadUrl.addOnSuccessListener {
                saveCommentData(comment, it.toString(), uid, postId)
            }
        }
    }

    fun saveCommentData(
        comment: String,
        imageUri: String,
        uid: String?,
        postId: String,
    ) {
        val timeStamp = System.currentTimeMillis().toString()
        val tweetData = Comment(comment, imageUri, timeStamp, uid!!, postId)
        database.getReference("comments").child(commentref.push().key!!).setValue(tweetData)
            .addOnSuccessListener {
                _isCommentPosted.value = true

            }.addOnFailureListener {
                _isCommentPosted.value = false
            }

    }

    private fun getCommentsAndUserData(onResult: (List<Pair<Comment, UserModel>>) -> Unit) {
        database.getReference("comments")
            .addListenerForSingleValueEvent(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {
                    val result = mutableListOf<Pair<Comment, UserModel>>()
                    for (commentSnapShot in snapshot.children) {
                        val comments = commentSnapShot.getValue(Comment::class.java)
                        comments?.let {
                            fetchUserDataFromComment(it) { user ->
                                result.add(0, it to user)
                                if (result.size == snapshot.childrenCount.toInt()) {
                                    onResult(result)
                                }
                            }
                        }
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }

    fun fetchUserDataFromComment(comment: Comment, onResult: (UserModel) -> Unit) {

        database.getReference("user").child(comment.userId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val userData = snapshot.getValue(UserModel::class.java)
                    userData?.let(onResult)
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }


    fun getSinglePostData(postId: String, onResult: (Pair<Post, UserModel>) -> Unit) {
        database.getReference("posts").child(postId)
            .addListenerForSingleValueEvent(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {
                    var result: Pair<Post, UserModel>? =null
                    val posts = snapshot.getValue(Post::class.java)
                    posts?.let {
                        getSingleUserDataFromPost(it) { user ->
                            result = it to user
                            onResult(result!!)
                        }
                    }
                }


                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }

    fun getSingleUserDataFromPost(post: Post, onResult: (UserModel) -> Unit) {

        database.getReference("user").child(post.userId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val userData = snapshot.getValue(UserModel::class.java)
                    userData?.let(onResult)
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }

}

