package ch.cern.eam.wshub.core.client;

import ch.cern.eam.wshub.core.interceptors.InforInterceptor;
import ch.cern.eam.wshub.core.interceptors.InforInvocationHandler;
import ch.cern.eam.wshub.core.services.administration.UserSetupService;
import ch.cern.eam.wshub.core.services.administration.impl.UserSetupServiceImpl;
import ch.cern.eam.wshub.core.services.comments.CommentService;
import ch.cern.eam.wshub.core.services.comments.impl.CommentServiceImpl;
import ch.cern.eam.wshub.core.services.documents.DocumentsService;
import ch.cern.eam.wshub.core.services.documents.impl.DocumentsServiceImpl;
import ch.cern.eam.wshub.core.services.equipment.*;
import ch.cern.eam.wshub.core.services.equipment.impl.*;
import ch.cern.eam.wshub.core.services.grids.GridsService;
import ch.cern.eam.wshub.core.services.grids.impl.GridsServiceImpl;
import ch.cern.eam.wshub.core.services.material.*;
import ch.cern.eam.wshub.core.services.material.impl.*;
import ch.cern.eam.wshub.core.services.workorders.CaseService;
import ch.cern.eam.wshub.core.services.workorders.CaseTaskService;
import ch.cern.eam.wshub.core.services.workorders.ChecklistService;
import ch.cern.eam.wshub.core.services.workorders.EmployeeService;
import ch.cern.eam.wshub.core.services.workorders.InspectionService;
import ch.cern.eam.wshub.core.services.workorders.LaborBookingService;
import ch.cern.eam.wshub.core.services.workorders.WorkOrderMiscService;
import ch.cern.eam.wshub.core.services.workorders.WorkOrderService;
import ch.cern.eam.wshub.core.services.workorders.impl.CaseServiceImpl;
import ch.cern.eam.wshub.core.services.workorders.impl.CaseTaskServiceImpl;
import ch.cern.eam.wshub.core.services.workorders.impl.ChecklistServiceImpl;
import ch.cern.eam.wshub.core.services.workorders.impl.EmployeeServiceImpl;
import ch.cern.eam.wshub.core.services.workorders.impl.InspectionServiceImpl;
import ch.cern.eam.wshub.core.services.workorders.impl.LaborBookingServiceImpl;
import ch.cern.eam.wshub.core.services.workorders.impl.WorkOrderMiscServiceImpl;
import ch.cern.eam.wshub.core.services.workorders.impl.WorkOrderServiceImpl;
import ch.cern.eam.wshub.core.tools.ApplicationData;
import ch.cern.eam.wshub.core.tools.Tools;

import net.datastream.wsdls.inforws.InforWebServicesPT;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Service;
import javax.xml.ws.handler.HandlerResolver;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.concurrent.ExecutorService;
import java.util.logging.Logger;


/**
 * Client for Infor services
 * This class is thread-safe. On single instance can and should be reused to handle multiple calls,
 * even if it involves different Infor users.
 */
public class InforClient implements Serializable {

    private Tools tools;
    private InvocationHandler invocationHandler;
    private InforWebServicesPT inforWebServicesToolkitClient;

    private CommentService commentService;
    private WorkOrderService workOrderService;
    private CaseService caseService;
    private CaseTaskService caseTaskService;
    private LaborBookingService laborBookingService;
    private WorkOrderMiscService workOrderMiscService;
    private EmployeeService employeeService;
    private ChecklistService checklistService;
    private InspectionService inspectionService;

    private AssetService assetService;
    private PositionService positionService;
    private SystemService systemService;
    private LocationService locationService;
    private EquipmentFacadeService equipmentFacadeService;

    private EquipmentStructureService equipmentStructureService;
    private LinearReferenceService linearReferenceService;
    private PMScheduleService pmScheduleService;
    private EquipmentWarrantyCoverageService equipmentWarrantyCoverageService;
    private EquipmentOtherService equipmentOtherService;

    private PartService partService;
    private PartKitService partKitService;
    private PartMiscService partMiscService;
    private PartStoreService partStoreService;
    private PartManufacturerService partManufacturerService;
    private PartBinStockService partBinStockService;
    private PurchaseOrdersService purchaseOrdersService;
    private PickTicketService pickTicketService;

    private UserSetupService userSetupService;
    private GridsService gridsService;
    private DocumentsService documentsService;

    private EquipmentGenerationService equipmentGenerationService;
    private EquipmentConfigurationService equipmentConfigurationService;

    // Prevent initializing the class without the builder
    private InforClient() {}

    public static class Builder {
        private String url;
        private String tenant;
        private String defaultOrganizationCode;
        private InforInterceptor inforInterceptor;
        private HandlerResolver soapHandlerResolver;
        private ExecutorService executorService;
        private DataSource dataSource;
        private EntityManagerFactory entityManagerFactory;
        private Logger logger;

        public Builder(String url, String tenant) {
            this.url = url;
            this.tenant = tenant;
        }

        public Builder withDefaultOrganizationCode(String defaultOrganizationCode) {
            this.defaultOrganizationCode = defaultOrganizationCode;
            return this;
        }

        public Builder withInforInterceptor(InforInterceptor inforInterceptor) {
            this.inforInterceptor = inforInterceptor;
            return this;
        }

        public Builder withSOAPHandlerResolver(HandlerResolver soapHandlerResolver) {
            this.soapHandlerResolver = soapHandlerResolver;
            return this;
        }

        public Builder withExecutorService(ExecutorService executorService) {
            this.executorService = executorService;
            return this;
        }

        public Builder withDataSource(DataSource dataSource) {
            this.dataSource = dataSource;
            return this;
        }

        public Builder withEntityManagerFactory(EntityManagerFactory entityManagerFactory) {
            this.entityManagerFactory = entityManagerFactory;
            return this;
        }

        public Builder withLogger(Logger logger) {
            this.logger = logger;
            return this;
        }

        private <T> T proxy(Class<T> targetClass, T target, InforInterceptor inforInterceptor, Tools tools) {
            return (T) Proxy.newProxyInstance(targetClass.getClassLoader(), new Class[] { targetClass }, new InforInvocationHandler<>(target, inforInterceptor, tools));
        }

        public InforClient build() {
            InforClient inforClient = new InforClient();

            // Application Data
            ApplicationData applicationData = new ApplicationData();
            applicationData.setUrl(this.url);
            applicationData.setOrganization(this.defaultOrganizationCode);
            applicationData.setTenant(this.tenant);

            // Infor Web Services Client
            Service service = Service.create(new QName("inforws"));
            if (this.soapHandlerResolver != null) {
                service.setHandlerResolver(soapHandlerResolver);
            }
            InforWebServicesPT inforWebServicesToolkitClient = service.getPort(InforWebServicesPT.class);
            ((BindingProvider) inforWebServicesToolkitClient).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, applicationData.getUrl());

            if (this.executorService != null) {
                // Init new Executor Service
            }

            // Tools
            Tools tools = new Tools(applicationData,
                    inforWebServicesToolkitClient,
                    this.executorService,
                    this.dataSource,
                    this.entityManagerFactory,
                    this.logger);
            inforClient.tools = tools;

            //
            // Init Service Classes
            //
            inforClient.workOrderService = proxy(WorkOrderService.class, new WorkOrderServiceImpl(applicationData, tools, inforWebServicesToolkitClient), inforInterceptor, tools);
            inforClient.commentService = proxy(CommentService.class, new CommentServiceImpl(applicationData, tools, inforWebServicesToolkitClient), inforInterceptor, tools);
            inforClient.caseService = proxy(CaseService.class, new CaseServiceImpl(applicationData, tools, inforWebServicesToolkitClient), inforInterceptor, tools);
            inforClient.caseTaskService = proxy(CaseTaskService.class, new CaseTaskServiceImpl(applicationData, tools, inforWebServicesToolkitClient), inforInterceptor, tools);
            inforClient.checklistService = proxy(ChecklistService.class, new ChecklistServiceImpl(applicationData, tools, inforWebServicesToolkitClient), inforInterceptor, tools);
            inforClient.inspectionService = proxy(InspectionService.class, new InspectionServiceImpl(applicationData, tools, inforWebServicesToolkitClient), inforInterceptor, tools);
            inforClient.laborBookingService = proxy(LaborBookingService.class, new LaborBookingServiceImpl(applicationData, tools, inforWebServicesToolkitClient), inforInterceptor, tools);
            inforClient.workOrderMiscService = proxy(WorkOrderMiscService.class, new WorkOrderMiscServiceImpl(applicationData, tools, inforWebServicesToolkitClient), inforInterceptor, tools);
            inforClient.employeeService = proxy(EmployeeService.class, new EmployeeServiceImpl(applicationData, tools, inforWebServicesToolkitClient), inforInterceptor, tools);
            inforClient.assetService = proxy(AssetService.class, new AssetServiceImpl(applicationData, tools, inforWebServicesToolkitClient), inforInterceptor, tools);
            inforClient.positionService = proxy(PositionService.class, new PositionServiceImpl(applicationData, tools, inforWebServicesToolkitClient), inforInterceptor, tools);
            inforClient.systemService = proxy(SystemService.class, new SystemServiceImpl(applicationData, tools, inforWebServicesToolkitClient), inforInterceptor, tools);
            inforClient.equipmentFacadeService = proxy(EquipmentFacadeService.class, new EquipmentFacadeServiceImpl(applicationData, tools, inforWebServicesToolkitClient), inforInterceptor, tools);
            inforClient.equipmentStructureService = proxy(EquipmentStructureService.class, new EquipmentStructureServiceImpl(applicationData, tools, inforWebServicesToolkitClient), inforInterceptor, tools);
            inforClient.linearReferenceService = proxy(LinearReferenceService.class, new LinearReferenceServiceImpl(applicationData, tools, inforWebServicesToolkitClient), inforInterceptor, tools);
            inforClient.pmScheduleService = proxy(PMScheduleService.class, new PMScheduleServiceImpl(applicationData, tools, inforWebServicesToolkitClient), inforInterceptor, tools);
            inforClient.equipmentWarrantyCoverageService = proxy(EquipmentWarrantyCoverageService.class, new EquipmentWarrantyCoverageServiceImpl(applicationData, tools, inforWebServicesToolkitClient), inforInterceptor, tools);
            inforClient.equipmentOtherService = proxy(EquipmentOtherService.class, new EquipmentOtherServiceImpl(applicationData, tools, inforWebServicesToolkitClient), inforInterceptor, tools);
            inforClient.partService = proxy(PartService.class, new PartServiceImpl(applicationData, tools, inforWebServicesToolkitClient), inforInterceptor, tools);
            inforClient.partMiscService = proxy(PartMiscService.class, new PartMiscServiceImpl(applicationData, tools, inforWebServicesToolkitClient), inforInterceptor, tools);
            inforClient.partStoreService = proxy(PartStoreService.class, new PartStoreServiceImpl(applicationData, tools, inforWebServicesToolkitClient), inforInterceptor, tools);
            inforClient.partManufacturerService = proxy(PartManufacturerService.class, new PartManufacturerServiceImpl(applicationData, tools, inforWebServicesToolkitClient), inforInterceptor, tools);
            inforClient.partBinStockService = proxy(PartBinStockService.class, new PartBinStockServiceImpl(applicationData, tools, inforWebServicesToolkitClient), inforInterceptor, tools);
            inforClient.locationService = proxy(LocationService.class, new LocationServiceImpl(applicationData, tools, inforWebServicesToolkitClient), inforInterceptor, tools);
            inforClient.partKitService = proxy(PartKitService.class, new PartKitServiceImpl(applicationData, tools, inforWebServicesToolkitClient), inforInterceptor, tools);
            inforClient.purchaseOrdersService = proxy(PurchaseOrdersService.class, new PurchaseOrdersImpl(applicationData, tools, inforWebServicesToolkitClient), inforInterceptor, tools);
            inforClient.userSetupService = proxy(UserSetupService.class, new UserSetupServiceImpl(applicationData, tools, inforWebServicesToolkitClient), inforInterceptor, tools);
            inforClient.gridsService = proxy(GridsService.class, new GridsServiceImpl(applicationData, tools, inforWebServicesToolkitClient), inforInterceptor, tools);
            inforClient.documentsService = proxy(DocumentsService.class, new DocumentsServiceImpl(applicationData, tools, inforWebServicesToolkitClient), inforInterceptor, tools);
            inforClient.pickTicketService = proxy(PickTicketService.class, new PickTicketServiceImpl(applicationData, tools, inforWebServicesToolkitClient), inforInterceptor, tools);
            inforClient.equipmentGenerationService = proxy(EquipmentGenerationService.class, new EquipmentGenerationServiceImpl(applicationData, tools, inforWebServicesToolkitClient),inforInterceptor, tools);
            inforClient.equipmentConfigurationService = proxy(EquipmentConfigurationService.class, new EquipmentConfigurationServiceImpl(applicationData, tools, inforWebServicesToolkitClient),inforInterceptor, tools);

            inforClient.inforWebServicesToolkitClient = inforWebServicesToolkitClient;
            return inforClient;
        }

    }

    //
    // GETTERS
    //
    public CommentService getCommentService() {
        return commentService;
    }

    public WorkOrderService getWorkOrderService() {
        return workOrderService;
    }

    public AssetService getAssetService() {
        return assetService;
    }

    public PositionService getPositionService() {
        return positionService;
    }

    public SystemService getSystemService() {
        return systemService;
    }

    public EquipmentFacadeService getEquipmentFacadeService() {
        return equipmentFacadeService;
    }

    public EquipmentStructureService getEquipmentStructureService() {
        return equipmentStructureService;
    }

    public LinearReferenceService getLinearReferenceService() {
        return linearReferenceService;
    }

    public PMScheduleService getPmScheduleService() {
        return pmScheduleService;
    }

    public EquipmentWarrantyCoverageService getEquipmentWarrantyCoverageService() {
        return equipmentWarrantyCoverageService;
    }

    public EquipmentOtherService getEquipmentOtherService() {
        return equipmentOtherService;
    }

    public PartService getPartService() {
        return partService;
    }

    public PartMiscService getPartMiscService() {
        return partMiscService;
    }

    public PartStoreService getPartStoreService() {
        return partStoreService;
    }

    public PartManufacturerService getPartManufacturerService() {
        return partManufacturerService;
    }

    public PartBinStockService getPartBinStockService() {
        return partBinStockService;
    }

    public LocationService getLocationService() {
        return locationService;
    }

    public CaseService getCaseService() {
        return caseService;
    }

    public LaborBookingService getLaborBookingService() {
        return laborBookingService;
    }

    public WorkOrderMiscService getWorkOrderMiscService() {
        return workOrderMiscService;
    }

    public EmployeeService getEmployeeService() {
        return employeeService;
    }

    public CaseTaskService getCaseTaskService() {
        return caseTaskService;
    }

    public ChecklistService getChecklistService() {
        return checklistService;
    }

    public InspectionService getInspectionService() {
        return inspectionService;
    }

    public UserSetupService getUserSetupService() {
        return userSetupService;
    }

    public GridsService getGridsService() {
        return gridsService;
    }

    public PartKitService getPartKitService() { return partKitService; }

    public PurchaseOrdersService getPurchaseOrdersService() { return purchaseOrdersService; }

    public DocumentsService getDocumentsService() {return documentsService; }

    public PickTicketService getPickTicketService() {return pickTicketService; }

    public InforWebServicesPT getInforWebServicesToolkitClient() {return inforWebServicesToolkitClient; }

    public Tools getTools() {
        return tools;
    }

    public EquipmentGenerationService getEquipmentGenerationService() {
        return equipmentGenerationService;
    }

    public EquipmentConfigurationService getEquipmentConfigurationService() {
        return equipmentConfigurationService;
    }
}
