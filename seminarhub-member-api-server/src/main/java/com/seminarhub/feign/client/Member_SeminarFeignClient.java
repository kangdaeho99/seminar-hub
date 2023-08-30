package com.seminarhub.feign.client;

import com.seminarhub.feign.config.Member_SeminarFeignClientConfiguration;
import com.seminarhub.dto.MemberSeminarDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * [2023-08-30 daeho.kang]
 * Description: Feign client interface for remote service communication.
 * This interface defines a Feign client to communicate with a remote service.
 * The `name` attribute is resolved from Spring Cloud Config to obtain the name of the service to communicate with.
 * Eureka Discovery is used to locate the URL of the corresponding service.
 */
@FeignClient(name = "${application.config.member-seminar-application-name}", configuration = {Member_SeminarFeignClientConfiguration.class})
public interface Member_SeminarFeignClient {

    /**
     * [2023-08-30 daeho.kang]
     * Description: Retrieve a list of MemberSeminarDTO objects by member ID.
     *
     * Calls the URL of the remote Member_Seminar service to retrieve Member_Seminar information related to the given member ID.
     */
    @GetMapping("/api/v1/member-seminar/findAllByMember_id/{member_id}")
    List<MemberSeminarDTO> findAllMember_SeminarByMember_id(@PathVariable("member_id") String member_id);

}
