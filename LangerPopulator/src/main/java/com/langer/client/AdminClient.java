package com.langer.client;

import com.langer.server.api.admin.AdminApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "admin", url = "${langer.server.baseUrl}")
public interface AdminClient extends AdminApi
{
}
