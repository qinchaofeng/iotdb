/**
 * Copyright © 2019 Apache IoTDB(incubating) (dev@iotdb.apache.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.iotdb.web.grafana.service.impl;

import org.apache.iotdb.tsfile.utils.Pair;
import org.apache.iotdb.web.grafana.bean.TimeValues;
import org.apache.iotdb.web.grafana.dao.BasicDao;
import org.apache.iotdb.web.grafana.service.DBConnectService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * Created by dell on 2017/7/17.
 */
@Service
public class DBConnectServiceImpl implements DBConnectService {

    @Autowired
    BasicDao basicDao;

    @Override
    public int testConnection() {
        return 0;
    }

    @Override
    public List<TimeValues> querySeries(String s, Pair<ZonedDateTime, ZonedDateTime> timeRange) {
        return basicDao.querySeries(s, timeRange);
    }

    @Override
    public List<String> getMetaData() {
        return basicDao.getMetaData();
    }

}
