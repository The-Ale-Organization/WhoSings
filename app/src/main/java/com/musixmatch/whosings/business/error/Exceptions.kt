package com.musixmatch.whosings.business.error

import java.lang.Exception

class AlreadyRegisteredUserException(): Exception()
class UserNotFoundException(): Exception()
class EmptyUserException(): Exception()