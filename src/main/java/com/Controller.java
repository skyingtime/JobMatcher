package com;

import com.model.Job;
import com.model.Worker;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by yangliu on 21/11/2016.
 */
@org.springframework.stereotype.Controller
@RequestMapping(value="/api")
public class Controller {

    @RequestMapping(value="/worker/{id}", method= RequestMethod.GET)
    public ResponseEntity<Job> getAppropiateJobsForWorker(@PathVariable("id") String id) {
        if(id != null) {
            RestTemplate restTemplate = new RestTemplate();
            Worker[] workerList = restTemplate.getForObject("http://swipejobs.azurewebsites.net/api/workers", Worker[].class);
            Job[] jobList = restTemplate.getForObject("http://swipejobs.azurewebsites.net/api/jobs", Job[].class);

            Worker worker = Arrays.stream(workerList).filter(workerItem -> workerItem.getUserId() == Integer.valueOf(id))
                                                                     .findFirst()
                                                                     .get();
            List<Job> selectedJobs = new ArrayList();
            for(Job job : jobList) {
               if((!job.isDriverLicenseRequired() || worker.isHasDriverLicense())
                   && worker.getCertificates().containsAll(job.getRequiredCertificates())
                   && isInSearchArea(job, worker)) {
                   selectedJobs.add(job);
               }
            }
            if(selectedJobs.size() > 3) {
                selectedJobs.sort((o1, o2) -> {
                    BigDecimal o1Rate = new BigDecimal(o1.getBillRate().substring(1));
                    BigDecimal o2Rate = new BigDecimal(o2.getBillRate().substring(1));
                    if(o1Rate.compareTo(o2Rate) < 0) {
                        return 1;
                    }
                    else if (o1Rate.compareTo(o2Rate) > 0){
                        return -1;
                    }
                    else {
                        if(o1.getWorkersRequired() < o2.getWorkersRequired()) {
                            return 1;
                        }
                        else if(o1.getWorkersRequired() > o2.getWorkersRequired()) {
                            return -1;
                        }
                        else {
                            return 0;
                        }
                    }
                });
                selectedJobs = selectedJobs.subList(0, 3);
            }
            return new ResponseEntity(selectedJobs, HttpStatus.OK);

        }
        return null;
    }

    //startDate availability

    private boolean isInSearchArea(Job job, Worker worker) {
        return worker.getJobSearchAddress().getMaxJobDistance() >= Math.sqrt(Math.pow(job.getLocation().getLatitude()-worker.getJobSearchAddress().getLatitude(), 2)
                + Math.pow(job.getLocation().getLongitude()-worker.getJobSearchAddress().getLongitude(), 2));
    }
}
