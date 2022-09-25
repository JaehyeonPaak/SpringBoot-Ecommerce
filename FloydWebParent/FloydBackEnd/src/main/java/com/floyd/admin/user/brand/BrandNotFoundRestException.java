package com.floyd.admin.user.brand;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Brand Not Found")
public class BrandNotFoundRestException extends Exception {
}
