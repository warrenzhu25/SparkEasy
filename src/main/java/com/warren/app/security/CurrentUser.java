package com.warren.app.security;

import com.warren.backend.data.entity.User;

@FunctionalInterface
public interface CurrentUser {

	User getUser();
}
