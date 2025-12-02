package enat.bank.share;

import enat.bank.Employee.Employee;
import enat.bank.utils.CommonService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ShareService extends CommonService<Share ,Long ,Share> {
    private final ShareRepository shareRepository;
    public ShareService(ShareRepository shareRepository) {
        super(shareRepository);
        this.shareRepository=shareRepository ;
    }
    public Optional<Share> getByEmployeeId(Long employeeId){
        return  shareRepository.findByEmployeeId(employeeId);
    }

    protected Share updateShare(Long id , Share share)
    {
        Share update=shareRepository.findByEmployeeId(share.getEmployeeId()).orElseThrow(()->
                new RuntimeException("Employee Not Found with  this ID:"+id)
        );
        update.setNoOfShare(share.getNoOfShare());
        update.setFullName(share.getFullName());
        update.setTotalSaving(share.getTotalSaving());
        return update ;
    }

}
