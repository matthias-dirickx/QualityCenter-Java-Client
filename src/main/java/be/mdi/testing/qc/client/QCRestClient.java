/*
 * QC REST API client
 *
 * Copyright (C) 2019  matthias.dirickx@outlook.com
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package be.mdi.testing.qc.client;

import be.mdi.testing.qc.model.QcType;
import be.mdi.testing.qc.model.entities.*;
import be.mdi.testing.qc.model.Domains;
import be.mdi.testing.qc.model.Projects;
import be.mdi.testing.qc.model.fields.QcAttachmentField;

/**
 * This class implemetns the {@Link RestCallHandler.Class} class.
 * The methods in this class aim to be readable.
 * The implementation may get verbose and repetitive.
 * A degree of verbosity and repetitiveness is tolerated to be able to offer a readable interface.
 */
public class QCRestClient {

    private RestCallHandler callHandler;

    /**
     * QCRestClient core instance.
     * To instantiate - Call the constructor.
     * After creating the object, you must log in by calling the login() method on this object.
     * To log out, the logout() method may be used. If not logging out, then the session may be active until expiration.
     * This is to be avoided.
     * @param host - String, host name - only the server, not the /qcbin/rest part!
     * @param username - Username in string format
     * @param password - password in string format (TODO not safe - check implementation)
     */
    public QCRestClient(String host, String username, String password) {
        callHandler = new RestCallHandler(host, username, password);
    }

    // Overhead & General items like logging in

    /**
     * Log in with the host, username and password provided in the constructor on creation of the object.
     * @return QCRestClient (fluent api)
     */
    public QCRestClient login() {
        callHandler.login();
        return this;
    }

    /**
     * Log out of the session.
     * Advised to do so to keep the sessions clean.
     */
    public void logout() { callHandler.logout(); }

    /**
     * Check if the client is logged in.
     * @return boolean (logged in: true; not logged in: false - true when is-authenticated returns OK.
     */
    public boolean isLoggedIn() { return callHandler.isLoggedIn(); }

    /**
     * Get the domains for the QC instance called (server address)
     * @return Domains
     */
    public Domains getDomains() {
        return callHandler.getRestData(Domains.class, "rest/domains");
    }

    /**
     * Get the domains with the project info included.
     * The object has the domains with the project names embedded per domain (good for selections)
     * @return Domains
     */
    public Domains getDomainsWithProjects() {
        return callHandler.getRestData(Domains.class, "rest/domains?include-projects-info=y");
    }

    /**
     * Get the list of projects for a specific domain.
     * @param domain String
     * @return Projects
     */
    public Projects getProjects(String domain) {
        return callHandler.getRestData(Projects.class, "rest/domains/" + domain + "/projects");
    }

    /**
     * Post an entity. Returns the HTTP status. (201 if OK (Created))
     * An entity is any object from any of the modules.
     * There is one generic Post per entity.
     * To get the created object returned, use postEntity(Class<T> retType, QcEntity entity).
     * @param entity QcEntity
     * @return Integer
     */
    public Integer postEntity(QcEntity entity) {
        return
        callHandler.postRestData(entity, entity.getUrl());
    }

    /**
     * Post an entity. Returns the object that is created or a marshalling error if sommething went wrong.
     * An entity is any object from any of the modules.
     * There is one generic Post for the entities.
     * If you don't need the object returned, but instead the HTTP code, then use postEntity(QcEntity entity).
     * @param retType Class
     * @param entity QcEntity
     * @param <T> Class
     * @return Class<T>
     */
    public <T> T postEntity(Class<T> retType, QcEntity entity) {
        return callHandler.postRestData(retType, entity, entity.getUrl());
    }

    /**
     * Post a set of entities. Returns HTTP status.
     * Limited to one type per set! So no mix of defect, test, run, run-step and so on.
     * If you want the response returned (the created object, that is),
     * then use postEntities(Class<T> retType, QcEntities, qcEntities).
     * @param entity QcEntities
     * @return Integer
     */
    public Integer postEntities(QcEntities entity) {
        return
                callHandler.postRestData(entity, entity.getUrl());
    }

    /**
     * Posts a set of entities. Returns the response (the created entities set).
     * Limited to one type per set! So no mix of defect, test, run, run-step and so on.
     * If you don't want the response, but the HTTP status code instead,
     * then use postEntities(QcEntities qcEntities).
     * @param retType Class<T>
     * @param qcEntities QcEntities
     * @param <T> Class
     * @return Class<T>
     */
    public <T> T postEntities(Class<T> retType, QcEntities qcEntities) {
        return callHandler.postRestData(retType, qcEntities, qcEntities.getUrl());
    }

    /**
     * Put an entity.
     * This provides an update. The call will fail when there is no ID provided.
     * @param entity QcEntity
     * @return Integer
     */
    public Integer putEntity(QcEntity entity) {
        return callHandler.putRestData(entity, entity.getUrl());
    }

    /**
     * Generic GET function.
     * The other getEntityX methods are derived from this one.
     * The idea is that if the logic changes profoundly we need to change this only in one place.
     *
     * The reason that the generic method is private and that the getEntityXYZ methods are defined verbosely:
     * To not have to include the return type in the method call explicitely each time.
     *
     * @param retType Class<T>
     * @param qcType QcType
     * @param domain String
     * @param project Project
     * @param entityId String (needs to be passed as string to url)
     * @param parentId String (needs to be passed as string to url)
     * @param <T> Return type - T extends QcEntity
     * @return <T extends QcEntity> - Any QC entity (if implemented properly, extending QcEntity)
     */
    private <T extends QcEntity> T getEntity(Class<T> retType, QcType qcType, String domain, String project, Integer entityId, Integer parentId) {

        T e = callHandler.getRestData(
                retType,
                getEntityGetUrl(qcType, domain, project, entityId, parentId)
        );

        e.setProject(project);
        e.setDomain(domain);
        return e;
    }

    /**
     * Get entities is a generic private function to get a collection of entities.
     *
     * @param retType Class<T>
     * @param qcType QcType
     * @param domain String
     * @param project String
     * @param <T> Class T - <T extends QcEntities & QcEntitiesInterface>
     * @return T extends QcEntities & QcEntitiesInterface
     */
    private <T extends QcEntities & QcEntitiesInterface> T getEntities(Class<T> retType, QcType qcType, String domain, String project) {

        T es = callHandler.getRestData(
                retType,
                getEntityGetUrl(qcType, domain, project, null, null)
        );

        es.setProject(project);
        es.setDomain(domain);
        return es;
    }

    /**
     * Deduce the URL from the information stored in the parametres passed to the client methods.
     * @param qcType QcType
     * @param domain String
     * @param project String
     * @param entityId String (may not be null)
     * @param parentId String (may be null)
     * @return
     */
    private String getEntityGetUrl(QcType qcType, String domain, String project, Integer entityId, Integer parentId) {
        String url = "rest/domains/" + domain + "/projects/" + project + "/";

        if(qcType.hasTypeParent() && parentId != null) {
            url += qcType.getParentType().getRestUrlType() + "/" + parentId + "/";
        }

        url += qcType.getRestUrlType();

        if(entityId != null) {
            url += "/" + entityId;
        }

        return url;
    }

    public QcDefect getDefect(String domain, String project, Integer defectId) {
        return getEntity(
                QcDefect.class,
                QcType.DEFECT,
                domain,
                project,
                defectId,
                null
                );
    }

    public QcDefects getDefects(String domain, String project) {
        return getEntities(
                QcDefects.class,
                QcType.DEFECT,
                domain,
                project
        );
    }

    public QcRun getRun(String domain, String project, int runId) {
        return getEntity(
                QcRun.class,
                QcType.RUN,
                domain,
                project,
                runId,
                null
        );
    }

    public QcRuns getRuns(String domain, String project) {
        return getEntities(
                QcRuns.class,
                QcType.RUN,
                domain,
                project
        );
    }

    public QcRunStep getRunStep(String domain, String project, int runId, int runStepId) {
        return getEntity(
                QcRunStep.class,
                QcType.RUN_STEP,
                domain,
                project,
                runStepId,
                runId
        );
    }

    public QcRunSteps getRunSteps(String domain, String project) {
        return getEntities(
                QcRunSteps.class,
                QcType.RUN_STEP,
                domain,
                project
        );
    }

    /**
     * Upsert attachment.
     * If no ID is found in the QcAttachment object: post attachment and update meta with rest of date via XML
     * If there is an id: PUT attachment and update meta with data via XML.
     * If there is no ID and no InputStream added ( a file ) --> Error
     * @param qcAttachment
     */
    public void postAttachment(QcAttachment qcAttachment) {

        String attId = qcAttachment.getField(QcAttachmentField.ID);
        QcAttachment qcCreated = null;

        if(qcAttachment.hasFile()) {
            if(attId == null) {
                qcCreated = callHandler.postAttachment(
                        qcAttachment.getAttachmentInputStream(),
                        qcAttachment.getFileName(),
                        qcAttachment.getUrl()
                );
                qcAttachment.setField(QcAttachmentField.ID, qcCreated.getField(QcAttachmentField.ID));
                putEntity(qcAttachment);
            } else {
                callHandler.putAttachment(
                        qcAttachment.getAttachmentInputStream(),
                        qcAttachment.getFileName(),
                        qcAttachment.getUrl()
                );
                putEntity(qcAttachment);
            }
        } else {
            if(attId == null) {
                throw new NullPointerException(
                        "When there is no ID for a QcAttachment, then the InputStream may not be null."
                );
            } else {
                putEntity(qcAttachment);
            }
        }
    }
}
