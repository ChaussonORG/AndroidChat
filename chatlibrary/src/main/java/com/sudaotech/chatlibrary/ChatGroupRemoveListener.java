package com.sudaotech.chatlibrary;

import com.hyphenate.EMGroupChangeListener;

/**
 * Created by Samuel on 2016/12/4 16:40
 * Email:xuzhou40@gmail.com
 * desc:
 */

public abstract class ChatGroupRemoveListener implements EMGroupChangeListener {
    @Override
    public void onInvitationReceived(String groupId, String groupName, String inviter, String reason) {

    }

    @Override
    public void onApplicationReceived(String groupId, String groupName, String applyer, String reason) {

    }

    @Override
    public void onApplicationAccept(String groupId, String groupName, String accepter) {

    }

    @Override
    public void onApplicationDeclined(String groupId, String groupName, String decliner, String reason) {

    }

    @Override
    public void onInvitationAccepted(String groupId, String inviter, String reason) {

    }

    @Override
    public void onInvitationDeclined(String groupId, String invitee, String reason) {

    }

    @Override
    public void onAutoAcceptInvitationFromGroup(String groupId, String inviter, String inviteMessage) {

    }
}
