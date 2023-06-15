package ntzw.mfl;

import ntzw.mfl.auth.AuthStatus;

public interface AuthOperation {

    AuthStatus auth(boolean retry);
}
