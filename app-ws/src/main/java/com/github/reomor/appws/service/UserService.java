package com.github.reomor.appws.service;

import com.github.reomor.appws.model.request.UserDetailsRequestModel;
import com.github.reomor.appws.model.response.WebUserModel;

public interface UserService {
    WebUserModel createUser(UserDetailsRequestModel userDetails);
}
