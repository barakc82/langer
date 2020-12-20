package com.langer.server.api.data;


import com.langer.server.api.admin.dto.*;
import com.langer.server.api.data.dto.Card;
import com.langer.server.api.data.dto.ConfirmIntroductionRequest;
import com.langer.server.api.data.dto.UserLanguageProgress;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequestMapping(path = "/data")
public interface DataApi
{
    String DATA_API_LANGUAGE                = "language";
    String DATA_API_USER                    = "user";
    String DATA_API_NEXT_CARD               = "next-card";
    String DATA_API_CONFIRM_INTRODUCTION    = "confirm-introduction";

    @RequestMapping(method = RequestMethod.GET, path = DATA_API_LANGUAGE)
    List<LanguageDto> getLanguages();

    @RequestMapping(method = RequestMethod.POST, path = DATA_API_NEXT_CARD)
    Card getNextCard(@RequestBody NextCardRequest nextCardRequest);

    @RequestMapping(method = RequestMethod.POST, path = DATA_API_CONFIRM_INTRODUCTION)
    Card confirmIntroduction(@RequestBody ConfirmIntroductionRequest confirmIntroductionRequest);
}