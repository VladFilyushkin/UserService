  package com.innowise.userservice.mapper;

  import com.innowise.userservice.dto.request.UserRequest;
  import com.innowise.userservice.dto.response.UserResponse;
  import com.innowise.userservice.entity.User;
  import org.mapstruct.Mapper;
  import org.mapstruct.ReportingPolicy;

  @Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
  public interface UserMapper {

    User fromUserRequestToUser(UserRequest userRequest);
    UserResponse fromUserToUserResponse(User user);


  }
