package blacksky.server.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.NOT_FOUND)
data class NotFoundException(override val message: String) : RuntimeException()

@ResponseStatus(HttpStatus.BAD_REQUEST)
data class BadRequestException(override val message: String) : RuntimeException()

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
data class InternalErrorException(override val message: String) : RuntimeException()

@ResponseStatus(HttpStatus.UNAUTHORIZED)
data class UnauthorizedException(override val message: String) : RuntimeException()

@ResponseStatus(HttpStatus.FORBIDDEN)
data class ForbiddenException(override val message: String) : RuntimeException()

@ResponseStatus(HttpStatus.CONFLICT)
data class ConflictException(override val message: String) : RuntimeException()