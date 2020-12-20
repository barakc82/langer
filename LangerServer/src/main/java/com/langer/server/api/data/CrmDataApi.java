package com.langer.server.api.data;


import org.springframework.web.bind.annotation.*;
//import com.langer.server.crm.data.api.dto.*;

@RequestMapping(path = "/api3/crm")
public interface CrmDataApi
{
    String CRM_API_PATH_SEARCH =                    "search";
    String CRM_API_PATH_COMMUNITY =                 "community";
    String CRM_API_PATH_CYCLE =                     "cycle";
    String CRM_API_PATH_CYCLE_SEARCH =              CRM_API_PATH_CYCLE + "/search";
    String CRM_API_PATH_COMMIT =                    "commit";
    String CRM_API_PATH_COURSE =                    "course";
    String CRM_API_PATH_FETCH_ALL =                 "fetch-all";
    String CRM_API_PATH_FETCH_TYPE =                "fetch-type";
    String CRM_API_PATH_FETCH_TYPE_COMPRESSED =     "fetch-type-gzip";
    String CRM_API_PATH_COURSE_OCC =                "course-occ";
    String CRM_API_PATH_PUBLISH_INFO =              "publish-info";
    String CRM_API_PATH_REGISTRATION =              "registration";
    String CRM_API_PATH_PARTICIPATION =             "participation";
    String CRM_API_PATH_COURSE_AND_REG =            "course-and-registrations";
    String CRM_API_PATH_PUBLIC_COURSE_OCC =         "public-course-occurrence";
/*
    @RequestMapping(method = RequestMethod.GET, path = CRM_API_PATH_SEARCH)
    List<SearchResultItem> search(@RequestParam(name = "query") String query);

 */
}