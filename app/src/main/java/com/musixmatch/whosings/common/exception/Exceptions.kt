package com.musixmatch.whosings.common.exception

import java.lang.Exception

class AlreadyRegisteredUserException: Exception()
class UserNotFoundException: Exception()
class EmptyUserException: Exception()

class MissingResponseFieldException(message: String): Exception()
class ErrorResponseException(status: String): Exception()

class InsufficientSongsNumberException: Exception()