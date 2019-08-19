package io.getstream.chat.example;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.getstream.sdk.chat.StreamChat;
import com.getstream.sdk.chat.adapter.ReactionDialogAdapter;
import com.getstream.sdk.chat.model.Attachment;
import com.getstream.sdk.chat.model.Channel;
import com.getstream.sdk.chat.model.ModelType;
import com.getstream.sdk.chat.rest.Message;
import com.getstream.sdk.chat.rest.core.Client;
import com.getstream.sdk.chat.utils.Constant;
import com.getstream.sdk.chat.utils.PermissionChecker;
import com.getstream.sdk.chat.utils.Utils;
import com.getstream.sdk.chat.utils.frescoimageviewer.ImageViewer;
import com.getstream.sdk.chat.view.MessageInputView;
import com.getstream.sdk.chat.view.MessageListView;
import com.getstream.sdk.chat.view.ReactionDlgView;
import com.getstream.sdk.chat.view.activity.AttachmentActivity;
import com.getstream.sdk.chat.viewmodel.ChannelViewModel;
import com.getstream.sdk.chat.viewmodel.ChannelViewModelFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.getstream.chat.example.databinding.ActivityChannelBinding;

/**
 * Show the messages for a channel
 */
public class ChannelActivity extends AppCompatActivity
        implements MessageListView.MessageClickListener,
        MessageListView.MessageLongClickListener,
        MessageListView.AttachmentClickListener,
        MessageInputView.OpenCameraViewListener {

    final String TAG = ChannelActivity.class.getSimpleName();

    private ChannelViewModel viewModel;
    private ActivityChannelBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // receive the intent and create a channel object
        Intent intent = getIntent();
        String channelType = intent.getStringExtra(MainActivity.EXTRA_CHANNEL_TYPE);
        String channelID = intent.getStringExtra(MainActivity.EXTRA_CHANNEL_ID);
        Client client = StreamChat.getInstance(getApplication());


        // we're using data binding in this example
        binding = DataBindingUtil.setContentView(this, R.layout.activity_channel);
        // most the business logic of the chat is handled in the ChannelViewModel view model
        binding.setLifecycleOwner(this);

        Channel channel = client.getChannelByCid(channelType + ":" + channelID);
        if (channel == null)
            channel = client.channel(channelType, channelID);
        viewModel = ViewModelProviders.of(this,
                new ChannelViewModelFactory(this.getApplication(), channel)
        ).get(ChannelViewModel.class);

        // connect the view model
        binding.channelHeader.setViewModel(viewModel, this);
        binding.channelHeader.setOnBackClickListener(v -> finish());

        MyMessageViewHolderFactory factory = new MyMessageViewHolderFactory();
        binding.messageList.setViewHolderFactory(factory);
        binding.messageList.setMessageClickListener(this);
        binding.messageList.setMessageLongClickListener(this);
        binding.messageList.setAttachmentClickListener(this);

        binding.messageInput.setViewModel(viewModel, this);
        binding.messageList.setViewModel(viewModel, this);
        binding.messageInput.setOpenCameraViewListener(this);
        // set the viewModel data for the activity_channel.xml layout
        binding.setViewModel(viewModel);
        // Permission Check
        PermissionChecker.permissionCheck(this, null);

    }

    @Override
    public void onBackPressed() {
        viewModel.removeEventHandler();
        super.onBackPressed();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == Constant.PERMISSIONS_REQUEST) {
            boolean granted = true;
            for (int grantResult : grantResults)
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    granted = false;
                    break;
                }
            if (!granted) PermissionChecker.showRationalDialog(this, null);
        }
    }

    @Override
    public void openCameraView(Intent intent, int REQUEST_CODE) {
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        binding.messageInput.progressCapturedMedia(requestCode, resultCode, data);
    }

    @Override
    public void onMessageClick(Message message, int position) {
        Log.i(TAG, "message was clicked");
        showReactionDialog(message, position);
    }

    @Override
    public void onAttachmentClick(Message message, Attachment attachment) {
        Log.i(TAG, "attachment was clicked");
        // Image

        if (attachment.getType().equals(ModelType.attach_image)) {
            List<String> imageUrls = new ArrayList<>();
            for (Attachment a : message.getAttachments()) {
                imageUrls.add(a.getImageURL());
            }

            int position = message.getAttachments().indexOf(attachment);

            new ImageViewer.Builder<>(this, imageUrls)
                    .setStartPosition(position)
                    .show();
        } else {
            // Giphy, Video, Link, Product,...
            Intent mediaIntent = new Intent(this, AttachmentActivity.class);
            this.startActivity(mediaIntent);
        }

    }

    List<String> reactionTypes = Arrays.asList("like", "love", "haha", "wow", "sad", "angry");

    public void showReactionDialog(Message message, int position) {
        int firstListItemPosition = ((LinearLayoutManager) binding.messageList.getLayoutManager()).findFirstVisibleItemPosition();
        final int lastListItemPosition = firstListItemPosition + binding.messageList.getChildCount() - 1;
        int childIndex;
        if (position < firstListItemPosition || position > lastListItemPosition) {
            childIndex = position;
        } else {
            childIndex = position - firstListItemPosition;
        }
        int originY = binding.messageList.getChildAt(childIndex).getBottom();

        final Dialog dialog = new Dialog(this); // Context, this, etc.
        ReactionDlgView reactionDlgView = new ReactionDlgView(this);


        reactionDlgView.setMessagewithStyle(binding.messageList.getChannel(),
                message, reactionTypes,
                binding.messageList.getStyle(),
                view -> dialog.dismiss()
        );

        dialog.setContentView(reactionDlgView);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        dialog.show();

        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.x = 0;
        int screenHeight = Utils.getScreenResolution(this);
        wlp.y = originY - screenHeight / 2;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
    }

    @Override
    public void onMessageLongClick(Message message, int position) {
        final Dialog dialog = new Dialog(this);
        if (!message.getUserId().equals(StreamChat.getInstance(this).getUserId()))
            dialog.setContentView(com.getstream.sdk.chat.R.layout.dialog_moreaction_incoming);
        else {
            dialog.setContentView(com.getstream.sdk.chat.R.layout.dialog_moreaction_outgoing);
            TextView tv_edit = dialog.findViewById(com.getstream.sdk.chat.R.id.tv_edit);
            TextView tv_delete = dialog.findViewById(com.getstream.sdk.chat.R.id.tv_delete);
            tv_edit.setOnClickListener((View v) -> {
                v.setTag(Constant.TAG_MOREACTION_EDIT);
                dialog.dismiss();
            });
            tv_delete.setOnClickListener((View v) -> {
                v.setTag(Constant.TAG_MOREACTION_DELETE);
                dialog.dismiss();
            });
        }


        RecyclerView rv_reaction = dialog.findViewById(com.getstream.sdk.chat.R.id.rv_reaction);
        TextView tv_reply = dialog.findViewById(com.getstream.sdk.chat.R.id.tv_reply);
        TextView tv_cancel = dialog.findViewById(com.getstream.sdk.chat.R.id.tv_cancel);
        RecyclerView.LayoutManager mLayoutManager;
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rv_reaction.setLayoutManager(mLayoutManager);
        ReactionDialogAdapter reactionAdapter = new ReactionDialogAdapter(binding.messageList.getChannel(),
                message,
                reactionTypes,
                false,
                binding.messageList.getStyle(),
                (View v) -> dialog.dismiss());
        rv_reaction.setAdapter(reactionAdapter);

        tv_reply.setOnClickListener((View v) -> {
            v.setTag(Constant.TAG_MOREACTION_REPLY);
            dialog.dismiss();
        });
        tv_cancel.setOnClickListener((View v) -> {
            dialog.dismiss();
        });

        dialog.show();

        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;

        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
    }
}