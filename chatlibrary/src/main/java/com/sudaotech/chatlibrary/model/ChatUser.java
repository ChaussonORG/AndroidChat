/**
 * Copyright (C) 2016 Hyphenate Inc. All rights reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sudaotech.chatlibrary.model;

import com.hyphenate.chat.EMContact;
import com.sudaotech.chatlibrary.utils.ChatUtil;

public class ChatUser extends EMContact {

    /**
     * initial letter for nickname
     */
    protected String initialLetter;
    /**
     * avatar of the user
     */
    protected String avatar;

    public ChatUser(String username) {
        this.username = username;
    }

    public String getInitialLetter() {
        if (initialLetter == null) {
            ChatUtil.setUserInitialLetter(this);
        }
        return initialLetter;
    }

    public void setInitialLetter(String initialLetter) {
        this.initialLetter = initialLetter;
    }


    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Override
    public int hashCode() {
        return 17 * getUsername().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || !(o instanceof ChatUser)) {
            return false;
        }
        return getUsername().equals(((ChatUser) o).getUsername());
    }

    @Override
    public String toString() {
        return nick == null ? username : nick;
    }
}
