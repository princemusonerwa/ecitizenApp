package com.minaloc.gov.service.impl;

import com.minaloc.gov.domain.Attachment;
import com.minaloc.gov.repository.AttachmentRepository;
import com.minaloc.gov.service.AttachmentService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Attachment}.
 */
@Service
@Transactional
public class AttachmentServiceImpl implements AttachmentService {

    private final Logger log = LoggerFactory.getLogger(AttachmentServiceImpl.class);

    private final AttachmentRepository attachmentRepository;

    public AttachmentServiceImpl(AttachmentRepository attachmentRepository) {
        this.attachmentRepository = attachmentRepository;
    }

    @Override
    public Attachment save(Attachment attachment) {
        log.debug("Request to save Attachment : {}", attachment);
        return attachmentRepository.save(attachment);
    }

    @Override
    public Attachment update(Attachment attachment) {
        log.debug("Request to save Attachment : {}", attachment);
        return attachmentRepository.save(attachment);
    }

    @Override
    public Optional<Attachment> partialUpdate(Attachment attachment) {
        log.debug("Request to partially update Attachment : {}", attachment);

        return attachmentRepository
            .findById(attachment.getId())
            .map(existingAttachment -> {
                if (attachment.getAttachmentLink() != null) {
                    existingAttachment.setAttachmentLink(attachment.getAttachmentLink());
                }
                if (attachment.getAttachmentLinkContentType() != null) {
                    existingAttachment.setAttachmentLinkContentType(attachment.getAttachmentLinkContentType());
                }
                if (attachment.getAttachmentType() != null) {
                    existingAttachment.setAttachmentType(attachment.getAttachmentType());
                }

                return existingAttachment;
            })
            .map(attachmentRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Attachment> findAll() {
        log.debug("Request to get all Attachments");
        return attachmentRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Attachment> findOne(Long id) {
        log.debug("Request to get Attachment : {}", id);
        return attachmentRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Attachment : {}", id);
        attachmentRepository.deleteById(id);
    }
}
