package uz.authentication.texyreaderml1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.mlkit.nl.smartreply.SmartReply
import com.google.mlkit.nl.smartreply.SmartReplySuggestion
import com.google.mlkit.nl.smartreply.SmartReplySuggestionResult
import com.google.mlkit.nl.smartreply.TextMessage

class ReplyActivity : AppCompatActivity() {
    lateinit var edtText: EditText
    lateinit var textView: TextView
    lateinit var button: Button
    lateinit var conversations: ArrayList<TextMessage>
    var userId = "123456"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reply)
        edtText = findViewById(R.id.edtMessage)
        textView = findViewById(R.id.tvMessage)
        button = findViewById(R.id.btnSend)


        conversations = ArrayList()

        val smartReplyGenerator = SmartReply.getClient()
        button.setOnClickListener {
            var message = edtText.text.toString().trim()
            conversations.add(
                TextMessage.createForRemoteUser(
                    message,
                    System.currentTimeMillis(),
                    userId
                )
            )
            smartReplyGenerator.suggestReplies(conversations)
                .addOnSuccessListener { result ->
                    if (result.status == SmartReplySuggestionResult.STATUS_NOT_SUPPORTED_LANGUAGE) {
                        textView.text = "Can't support Language"
                    } else if (result.status == SmartReplySuggestionResult.STATUS_SUCCESS) {
                        var reply:String = ""
                        for (suggestion: SmartReplySuggestion in result.suggestions) {
                           reply = "${reply+suggestion.text}\n"
                        }
                        textView.text = reply
                    }
                }
                .addOnFailureListener {
                    textView.text = "Failed"
                }
        }

    }
}