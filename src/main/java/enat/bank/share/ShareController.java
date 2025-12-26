package enat.bank.share;

import enat.bank.exception.ShareException;
import enat.bank.utils.Common;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
@RestController
@RequestMapping("api/share")
@RequiredArgsConstructor
public class ShareController implements Common<Share, String, String, Share> {
    private  final ShareService shareService;

    @Override
    public ResponseEntity<Share> create(Share share)  {

    if (shareService.getByEmployeeId(share.getEmployeeId()).isPresent()){

        throw  new ShareException("this share already Registered !");

    }

    return  shareService.create(share);
    }

    @Override
    public ResponseEntity<Share> update(Long id, Share share) {
        return shareService.update(shareService.updateShare(id,share),id);
    }

    @Override
    public void delete(Long id) {
     shareService.delete(id);
    }

    @Override
    public Optional<Share> getById(Long id) {
        return shareService.getById(id);
    }

    @Override
    public Page<Share> getAllPageable(Pageable pageable, String name) {
        return shareService.getAllPageable(pageable);
    }


@PostMapping("import-csv")
    public List<Share> importCsv(@RequestParam("file")  MultipartFile multipartFile){
        return shareService.importCsv(multipartFile);
}



}
