package github.com.worldmaomao.servcie.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import github.com.worldmaomao.config.Config;
import github.com.worldmaomao.http.HttpUtil;
import github.com.worldmaomao.servcie.DiskItemService;
import github.com.worldmaomao.vo.*;
import lombok.Builder;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 */

@Builder
public class DiskItemServiceImpl implements DiskItemService {
    private static final String DISK_ITEM_URL = "/api/v1/disk-item";

    private Config config;

    public DiskItemServiceImpl(Config config) {
        this.config = config;
    }

    public QueryDiskItemResponseVo query(QueryDiskItemRequestVo vo) throws IOException {
        String url = String.format(config.getServerUrl() + DISK_ITEM_URL);
        Map<String, String> params = new HashMap();
        params.put("page", String.valueOf(vo.getPage()));
        params.put("pageSize", String.valueOf(vo.getPageSize()));
        params.put("diskId", vo.getDiskId());
        params.put("fileName", vo.getFileName());
        ResponseVo response = new HttpUtil().get(url, params);
        if (response.getCode() == 0) {
            JSONArray array = (JSONArray) (((JSONObject) response.getData()).get("list"));
            List<DiskItemVo> voList = null;
            if (array == null || array.isEmpty()) {
                voList = new ArrayList();
            } else {
                voList = array.toJavaList(DiskItemVo.class);
            }
            QueryDiskItemResponseVo responseVo = new QueryDiskItemResponseVo();
            responseVo.setList(voList);
            responseVo.setPage((((JSONObject) response.getData()).getInteger("page")));
            responseVo.setPageSize((((JSONObject) response.getData()).getInteger("size")));
            responseVo.setTotalCount((((JSONObject) response.getData()).getInteger("total")));
            return responseVo;
        }
        return null;
    }

    @Override
    public List<ImportDiskItemResultVo> batchImport(List<ImportDiskItemVo> itemVoList) throws IOException {
        String url = String.format(config.getServerUrl() + DISK_ITEM_URL);
        byte[] body = JSONArray.toJSONBytes(itemVoList);
        ResponseVo response = new HttpUtil().post(url, body);
        if (response.getCode() == 0) {
            JSONArray array = (JSONArray) response.getData();
            if (array == null || array.isEmpty()) {
                return new ArrayList<>();
            } else {
                return array.toJavaList(ImportDiskItemResultVo.class);
            }
        }
        return new ArrayList<>();
    }

    @Override
    public List<DeleteDiskItemResultVo> batchDelete(List<String> diskItemIdList) throws IOException {
        String url = String.format(config.getServerUrl() + DISK_ITEM_URL);
        String diskItemIds = StringUtils.join(diskItemIdList, ",");
        Map<String, String> params = new HashMap<>();
        params.put("id", diskItemIds);
        ResponseVo response = new HttpUtil().delete(url, params);
        if (response.getCode() == 0) {
            JSONArray array = (JSONArray) response.getData();
            if (array == null || array.isEmpty()) {
                return new ArrayList<>();
            } else {
                return array.toJavaList(DeleteDiskItemResultVo.class);
            }
        }
        return new ArrayList<>();
    }
}
