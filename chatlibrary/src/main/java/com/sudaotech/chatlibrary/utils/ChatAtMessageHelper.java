package com.sudaotech.chatlibrary.utils;

import android.text.TextUtils;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessage.ChatType;
import com.sudaotech.chatlibrary.ChatConstant;
import com.sudaotech.chatlibrary.model.ChatUser;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ChatAtMessageHelper {
    private static ChatAtMessageHelper instance = null;
    private List<String> toAtUserList = new ArrayList<String>();
    private Set<String> atMeGroupList = null;

    private ChatAtMessageHelper() {
//        atMeGroupList = ChatPreferenceManager.getInstance().getAtMeGroups();
//        if (atMeGroupList == null)
//            atMeGroupList = new HashSet<String>();

    }

    public synchronized static ChatAtMessageHelper get() {
        if (instance == null) {
            instance = new ChatAtMessageHelper();
        }
        return instance;
    }

    /**
     * add user you want to @
     *
     * @param username
     */
    public void addAtUser(String username) {
        synchronized (toAtUserList) {
            if (!toAtUserList.contains(username)) {
                toAtUserList.add(username);
            }
        }

    }

    /**
     * check if be mentioned(@) in the content
     *
     * @param content
     * @return
     */
    public boolean containsAtUsername(String content) {
        if (TextUtils.isEmpty(content)) {
            return false;
        }
        synchronized (toAtUserList) {
            for (String username : toAtUserList) {
                String nick = username;
                if (ChatUserUtils.getUserInfo(username) != null) {
                    ChatUser user = ChatUserUtils.getUserInfo(username);
                    if (user != null) {
                        nick = user.getNick();
                    }
                }
                if (content.contains(nick)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean containsAtAll(String content) {
//        String atAll = "@" + ChatUI.getInstance().getContext().getString(R.string.all_members);
//        if (content.contains(atAll)) {
//            return true;
//        }
        return false;
    }

    /**
     * get the users be mentioned(@)
     *
     * @param content
     * @return
     */
    public List<String> getAtMessageUsernames(String content) {
        if (TextUtils.isEmpty(content)) {
            return null;
        }
        synchronized (toAtUserList) {
            List<String> list = null;
            for (String username : toAtUserList) {
                String nick = username;
                if (ChatUserUtils.getUserInfo(username) != null) {
                    ChatUser user = ChatUserUtils.getUserInfo(username);
                    if (user != null) {
                        nick = user.getNick();
                    }
                }
                if (content.contains(nick)) {
                    if (list == null) {
                        list = new ArrayList<String>();
                    }
                    list.add(username);
                }
            }
            return list;
        }
    }

    /**
     * parse the mBaseMessage, get and save group id if I was mentioned(@)
     *
     * @param messages
     */
    public void parseMessages(List<EMMessage> messages) {
        int size = atMeGroupList.size();
        EMMessage[] msgs = messages.toArray(new EMMessage[messages.size()]);
        for (EMMessage msg : msgs) {
            if (msg.getChatType() == ChatType.GroupChat) {
                String groupId = msg.getTo();
                try {
                    JSONArray jsonArray = msg.getJSONArrayAttribute(ChatConstant.MESSAGE_ATTR_AT_MSG);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        String username = jsonArray.getString(i);
                        if (EMClient.getInstance().getCurrentUser().equals(username)) {
                            if (!atMeGroupList.contains(groupId)) {
                                atMeGroupList.add(groupId);
                                break;
                            }
                        }
                    }
                } catch (Exception e1) {
                    //Determine whether is @ all mBaseMessage
                    String usernameStr = msg.getStringAttribute(ChatConstant.MESSAGE_ATTR_AT_MSG, null);
                    if (usernameStr != null) {
                        if (usernameStr.toUpperCase().equals(ChatConstant.MESSAGE_ATTR_VALUE_AT_MSG_ALL)) {
                            if (!atMeGroupList.contains(groupId)) {
                                atMeGroupList.add(groupId);
                            }
                        }
                    }
                }

//                if (atMeGroupList.size() != size) {
//                    ChatPreferenceManager.getInstance().setAtMeGroups(atMeGroupList);
//                }
            }
        }
    }

    /**
     * get groups which I was mentioned
     *
     * @return
     */
    public Set<String> getAtMeGroups() {
        return atMeGroupList;
    }

    /**
     * remove group from the list
     *
     * @param groupId
     */
    public void removeAtMeGroup(String groupId) {
        if (atMeGroupList.contains(groupId)) {
            atMeGroupList.remove(groupId);
//            ChatPreferenceManager.getInstance().setAtMeGroups(atMeGroupList);
        }
    }

    /**
     * check if the input groupId in atMeGroupList
     *
     * @param groupId
     * @return
     */
    public boolean hasAtMeMsg(String groupId) {
        return atMeGroupList.contains(groupId);
    }

    public boolean isAtMeMsg(EMMessage message) {
        ChatUser user = ChatUserUtils.getUserInfo(message.getFrom());
        if (user != null) {
            try {
                JSONArray jsonArray = message.getJSONArrayAttribute(ChatConstant.MESSAGE_ATTR_AT_MSG);

                for (int i = 0; i < jsonArray.length(); i++) {
                    String username = jsonArray.getString(i);
                    if (username.equals(EMClient.getInstance().getCurrentUser())) {
                        return true;
                    }
                }
            } catch (Exception e) {
                //perhaps is a @ all mBaseMessage
                String atUsername = message.getStringAttribute(ChatConstant.MESSAGE_ATTR_AT_MSG, null);
                if (atUsername != null) {
                    if (atUsername.toUpperCase().equals(ChatConstant.MESSAGE_ATTR_VALUE_AT_MSG_ALL)) {
                        return true;
                    }
                }
                return false;
            }

        }
        return false;
    }

    public JSONArray atListToJsonArray(List<String> atList) {
        JSONArray jArray = new JSONArray();
        int size = atList.size();
        for (int i = 0; i < size; i++) {
            String username = atList.get(i);
            jArray.put(username);
        }
        return jArray;
    }

    public void cleanToAtUserList() {
        synchronized (toAtUserList) {
            toAtUserList.clear();
        }
    }
}
