/*
 * Copyright 2012-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *	  https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package homeautomation.schedulers;


import homeautomation.auroraclient.client.EnumPathParamDataType;
import homeautomation.auroraclient.client.EnumQueryParamSampleSize;
import homeautomation.services.PowerPlantService;
import java.text.SimpleDateFormat;
import java.util.Date;


import homeautomation.services.PowerplantTimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTasks {
    
	private static final Logger LOG = LoggerFactory.getLogger(ScheduledTasks.class);

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

        @Autowired 
        private PowerPlantService powerPlantService;

	@Scheduled(fixedRate = 5000)
	public void collectPowerData() {
		PowerplantTimeUtils s;
		LOG.info("The time is now {}", dateFormat.format(new Date()));
                powerPlantService.collectDailyGeneratedPower(
						PowerplantTimeUtils.getToday(),
						PowerplantTimeUtils.getDayRelativeToToday(1),
						EnumPathParamDataType.POWER,
						EnumQueryParamSampleSize.MIN15
				);
        }
}
