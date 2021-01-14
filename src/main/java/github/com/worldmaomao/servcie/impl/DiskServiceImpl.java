package github.com.worldmaomao.servcie.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import github.com.worldmaomao.config.Config;
import github.com.worldmaomao.http.HttpUtil;
import github.com.worldmaomao.servcie.DiskService;
import github.com.worldmaomao.vo.DiskCreateVo;
import github.com.worldmaomao.vo.DiskVo;
import github.com.worldmaomao.vo.ResponseVo;
import github.com.worldmaomao.vo.ServiceResultVo;
import lombok.Builder;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 */
@Builder
public class DiskServiceImpl implements DiskService {
    private static final String DISK_URL = "/api/v1/disk";

    private Config config;

    public DiskServiceImpl(Config config) {
        this.config = config;
    }


    public List<DiskVo> queryAll() throws IOException {
        String url = String.format(config.getServerUrl() + DISK_URL);
        Map<String, String> params = new HashMap();
        params.put("page", "1");
        params.put("pageSize", "99999");
        ResponseVo response = new HttpUtil().get(url, params);
        if (response.getCode() == 0) {
            JSONArray array = (JSONArray) (((JSONObject) response.getData()).get("list"));
            List<DiskVo> voList = array.toJavaList(DiskVo.class);
            //System.out.println(voList);
            return voList;
        }
        return null;
    }

    @Override
    public ServiceResultVo delete(String diskId) throws IOException {
        String url = String.format(config.getServerUrl() + DISK_URL);
        Map<String, String> params = new HashMap();
        params.put("id", diskId);
        ResponseVo response = new HttpUtil().delete(url, params);
        ServiceResultVo resultVo = new ServiceResultVo();
        resultVo.setSuccess(response.getCode() == 0);
        resultVo.setError(response.getMessage());
        return resultVo;
    }

    @Override
    public ServiceResultVo create(DiskCreateVo vo) throws IOException {
        String url = String.format(config.getServerUrl() + DISK_URL);
        byte[] body = JSON.toJSONBytes(vo);
        ResponseVo response = new HttpUtil().post(url, body);
        ServiceResultVo resultVo = new ServiceResultVo();
        resultVo.setSuccess(response.getCode() == 0);
        resultVo.setError(response.getMessage());
        return resultVo;
    }
}
