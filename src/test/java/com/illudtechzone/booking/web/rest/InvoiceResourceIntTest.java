package com.illudtechzone.booking.web.rest;

import com.illudtechzone.booking.BookingApp;

import com.illudtechzone.booking.domain.Invoice;
import com.illudtechzone.booking.repository.InvoiceRepository;
import com.illudtechzone.booking.repository.search.InvoiceSearchRepository;
import com.illudtechzone.booking.service.InvoiceService;
import com.illudtechzone.booking.service.dto.InvoiceDTO;
import com.illudtechzone.booking.service.mapper.InvoiceMapper;
import com.illudtechzone.booking.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;


import static com.illudtechzone.booking.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the InvoiceResource REST controller.
 *
 * @see InvoiceResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BookingApp.class)
public class InvoiceResourceIntTest {

    private static final Long DEFAULT_DISTANCE = 1L;
    private static final Long UPDATED_DISTANCE = 2L;

    private static final String DEFAULT_PICKUP_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_PICKUP_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_DESTINATION_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_DESTINATION_ADDRESS = "BBBBBBBBBB";

    private static final Long DEFAULT_COST = 1L;
    private static final Long UPDATED_COST = 2L;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private InvoiceMapper invoiceMapper;

    @Autowired
    private InvoiceService invoiceService;

    /**
     * This repository is mocked in the com.illudtechzone.booking.repository.search test package.
     *
     * @see com.illudtechzone.booking.repository.search.InvoiceSearchRepositoryMockConfiguration
     */
    @Autowired
    private InvoiceSearchRepository mockInvoiceSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restInvoiceMockMvc;

    private Invoice invoice;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final InvoiceResource invoiceResource = new InvoiceResource(invoiceService);
        this.restInvoiceMockMvc = MockMvcBuilders.standaloneSetup(invoiceResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Invoice createEntity(EntityManager em) {
        Invoice invoice = new Invoice()
            .distance(DEFAULT_DISTANCE)
            .pickupAddress(DEFAULT_PICKUP_ADDRESS)
            .destinationAddress(DEFAULT_DESTINATION_ADDRESS)
            .cost(DEFAULT_COST);
        return invoice;
    }

    @Before
    public void initTest() {
        invoice = createEntity(em);
    }

    @Test
    @Transactional
    public void createInvoice() throws Exception {
        int databaseSizeBeforeCreate = invoiceRepository.findAll().size();

        // Create the Invoice
        InvoiceDTO invoiceDTO = invoiceMapper.toDto(invoice);
        restInvoiceMockMvc.perform(post("/api/invoices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoiceDTO)))
            .andExpect(status().isCreated());

        // Validate the Invoice in the database
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeCreate + 1);
        Invoice testInvoice = invoiceList.get(invoiceList.size() - 1);
        assertThat(testInvoice.getDistance()).isEqualTo(DEFAULT_DISTANCE);
        assertThat(testInvoice.getPickupAddress()).isEqualTo(DEFAULT_PICKUP_ADDRESS);
        assertThat(testInvoice.getDestinationAddress()).isEqualTo(DEFAULT_DESTINATION_ADDRESS);
        assertThat(testInvoice.getCost()).isEqualTo(DEFAULT_COST);

        // Validate the Invoice in Elasticsearch
        verify(mockInvoiceSearchRepository, times(1)).save(testInvoice);
    }

    @Test
    @Transactional
    public void createInvoiceWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = invoiceRepository.findAll().size();

        // Create the Invoice with an existing ID
        invoice.setId(1L);
        InvoiceDTO invoiceDTO = invoiceMapper.toDto(invoice);

        // An entity with an existing ID cannot be created, so this API call must fail
        restInvoiceMockMvc.perform(post("/api/invoices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoiceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Invoice in the database
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeCreate);

        // Validate the Invoice in Elasticsearch
        verify(mockInvoiceSearchRepository, times(0)).save(invoice);
    }

    @Test
    @Transactional
    public void getAllInvoices() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList
        restInvoiceMockMvc.perform(get("/api/invoices?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(invoice.getId().intValue())))
            .andExpect(jsonPath("$.[*].distance").value(hasItem(DEFAULT_DISTANCE.intValue())))
            .andExpect(jsonPath("$.[*].pickupAddress").value(hasItem(DEFAULT_PICKUP_ADDRESS.toString())))
            .andExpect(jsonPath("$.[*].destinationAddress").value(hasItem(DEFAULT_DESTINATION_ADDRESS.toString())))
            .andExpect(jsonPath("$.[*].cost").value(hasItem(DEFAULT_COST.intValue())));
    }
    
    @Test
    @Transactional
    public void getInvoice() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get the invoice
        restInvoiceMockMvc.perform(get("/api/invoices/{id}", invoice.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(invoice.getId().intValue()))
            .andExpect(jsonPath("$.distance").value(DEFAULT_DISTANCE.intValue()))
            .andExpect(jsonPath("$.pickupAddress").value(DEFAULT_PICKUP_ADDRESS.toString()))
            .andExpect(jsonPath("$.destinationAddress").value(DEFAULT_DESTINATION_ADDRESS.toString()))
            .andExpect(jsonPath("$.cost").value(DEFAULT_COST.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingInvoice() throws Exception {
        // Get the invoice
        restInvoiceMockMvc.perform(get("/api/invoices/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInvoice() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        int databaseSizeBeforeUpdate = invoiceRepository.findAll().size();

        // Update the invoice
        Invoice updatedInvoice = invoiceRepository.findById(invoice.getId()).get();
        // Disconnect from session so that the updates on updatedInvoice are not directly saved in db
        em.detach(updatedInvoice);
        updatedInvoice
            .distance(UPDATED_DISTANCE)
            .pickupAddress(UPDATED_PICKUP_ADDRESS)
            .destinationAddress(UPDATED_DESTINATION_ADDRESS)
            .cost(UPDATED_COST);
        InvoiceDTO invoiceDTO = invoiceMapper.toDto(updatedInvoice);

        restInvoiceMockMvc.perform(put("/api/invoices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoiceDTO)))
            .andExpect(status().isOk());

        // Validate the Invoice in the database
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeUpdate);
        Invoice testInvoice = invoiceList.get(invoiceList.size() - 1);
        assertThat(testInvoice.getDistance()).isEqualTo(UPDATED_DISTANCE);
        assertThat(testInvoice.getPickupAddress()).isEqualTo(UPDATED_PICKUP_ADDRESS);
        assertThat(testInvoice.getDestinationAddress()).isEqualTo(UPDATED_DESTINATION_ADDRESS);
        assertThat(testInvoice.getCost()).isEqualTo(UPDATED_COST);

        // Validate the Invoice in Elasticsearch
        verify(mockInvoiceSearchRepository, times(1)).save(testInvoice);
    }

    @Test
    @Transactional
    public void updateNonExistingInvoice() throws Exception {
        int databaseSizeBeforeUpdate = invoiceRepository.findAll().size();

        // Create the Invoice
        InvoiceDTO invoiceDTO = invoiceMapper.toDto(invoice);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInvoiceMockMvc.perform(put("/api/invoices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoiceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Invoice in the database
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Invoice in Elasticsearch
        verify(mockInvoiceSearchRepository, times(0)).save(invoice);
    }

    @Test
    @Transactional
    public void deleteInvoice() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        int databaseSizeBeforeDelete = invoiceRepository.findAll().size();

        // Delete the invoice
        restInvoiceMockMvc.perform(delete("/api/invoices/{id}", invoice.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Invoice in Elasticsearch
        verify(mockInvoiceSearchRepository, times(1)).deleteById(invoice.getId());
    }

    @Test
    @Transactional
    public void searchInvoice() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);
        when(mockInvoiceSearchRepository.search(queryStringQuery("id:" + invoice.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(invoice), PageRequest.of(0, 1), 1));
        // Search the invoice
        restInvoiceMockMvc.perform(get("/api/_search/invoices?query=id:" + invoice.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(invoice.getId().intValue())))
            .andExpect(jsonPath("$.[*].distance").value(hasItem(DEFAULT_DISTANCE.intValue())))
            .andExpect(jsonPath("$.[*].pickupAddress").value(hasItem(DEFAULT_PICKUP_ADDRESS)))
            .andExpect(jsonPath("$.[*].destinationAddress").value(hasItem(DEFAULT_DESTINATION_ADDRESS)))
            .andExpect(jsonPath("$.[*].cost").value(hasItem(DEFAULT_COST.intValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Invoice.class);
        Invoice invoice1 = new Invoice();
        invoice1.setId(1L);
        Invoice invoice2 = new Invoice();
        invoice2.setId(invoice1.getId());
        assertThat(invoice1).isEqualTo(invoice2);
        invoice2.setId(2L);
        assertThat(invoice1).isNotEqualTo(invoice2);
        invoice1.setId(null);
        assertThat(invoice1).isNotEqualTo(invoice2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(InvoiceDTO.class);
        InvoiceDTO invoiceDTO1 = new InvoiceDTO();
        invoiceDTO1.setId(1L);
        InvoiceDTO invoiceDTO2 = new InvoiceDTO();
        assertThat(invoiceDTO1).isNotEqualTo(invoiceDTO2);
        invoiceDTO2.setId(invoiceDTO1.getId());
        assertThat(invoiceDTO1).isEqualTo(invoiceDTO2);
        invoiceDTO2.setId(2L);
        assertThat(invoiceDTO1).isNotEqualTo(invoiceDTO2);
        invoiceDTO1.setId(null);
        assertThat(invoiceDTO1).isNotEqualTo(invoiceDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(invoiceMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(invoiceMapper.fromId(null)).isNull();
    }
}
