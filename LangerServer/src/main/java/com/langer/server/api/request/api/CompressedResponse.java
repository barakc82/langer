package swingit.server.crm.request.api;

import lombok.Data;

@Data
public class CompressedResponse
{
    private byte[] payload;
}
