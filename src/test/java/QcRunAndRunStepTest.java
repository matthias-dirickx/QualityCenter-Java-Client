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
import be.mdi.testing.qc.model.composits.QcRunAndRunSteps;
import be.mdi.testing.qc.model.entities.QcRun;
import be.mdi.testing.qc.model.entities.QcRunStep;
import be.mdi.testing.qc.model.fields.QcRunField;
import be.mdi.testing.qc.model.fields.QcRunStepField;
import org.junit.jupiter.api.Test;

import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

public class QcRunAndRunStepTest extends BaseMockTest {

    @Test
    public void testThatWeCanUseTheCommitFunctionality() {

        mockServer
                .when(request("/qcbin/rest/domains/theDomain/projects/theProject/runs")
                        .withBody("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
                                "<Entity Type=\"run\">" +
                                "<Fields>" +
                                "<Field Name=\"execution-date\">" +
                                "<Value>2019-10-27</Value>" +
                                "</Field>" +
                                "<Field Name=\"run-name\">" +
                                "<Value>the run name</Value>" +
                                "</Field>" +
                                "</Fields>" +
                                "</Entity>"))

                .respond(response()
                        .withHeader("Content-Type", "application/xml")
                        .withBody("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
                                "<Entity Type=\"run\">" +
                                "<Fields>" +
                                "<Field Name=\"id\">" +
                                "<Value>1</Value>" +
                                "</Field>" +
                                "<Field Name=\"run-name\">" +
                                "<Value>the run name</Value>" +
                                "</Field>" +
                                "<Field Name=\"execution-date\">" +
                                "<Value>2019-07-20</Value>" +
                                "</Field>" +
                                "</Fields>" +
                                "</Entity>")
                        .withStatusCode(201)
                        .withCookie("some-cookie", "to avoid default"));

        mockServer
                .when(request("/qcbin/rest/domains/theDomain/projects/theProject/runs/1/run-steps")
                        .withBody("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
                                "<Entities>" +
                                "<Entity Type=\"run-step\">" +
                                "<Fields>" +
                                "<Field Name=\"run-id\"><Value>1</Value></Field>" +
                                "<Field Name=\"run-name\">" +
                                "<Value>the description</Value>" +
                                "</Field>" +
                                "<Field Name=\"execution-date\">" +
                                "<Value>2019-07-20</Value>" +
                                "</Field>" +
                                "</Fields>" +
                                "</Entity>" +
                                "</Entities>"))

                .respond(response()
                        .withHeader("Content-Type", "application/xml")
                        .withBody("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
                                "<Entities>" +
                                "<Entity Type=\"run-step\">" +
                                "<Fields>" +
                                "<Field Name=\"id\"><Value>1</Value></Field>" +
                                "<Field Name=\"run-id\"><Value>1</Value></Field>" +
                                "<Field Name=\"run-name\">" +
                                "<Value>the description</Value>" +
                                "</Field>" +
                                "<Field Name=\"execution-date\">" +
                                "<Value>2019-07-27</Value>" +
                                "</Field>" +
                                "</Fields>" +
                                "</Entity>" +
                                "</Entities>")
                        .withStatusCode(201)
                        .withCookie("some-cookie", "to avoid default"));

        new QcRunAndRunSteps(
                new QcRun()
                        .setField(QcRunField.RUN_NAME, "the run name")
                        .setField(QcRunField.EXECUTION_DATE, "2019-10-27")
        )
                .addStep(new QcRunStep().setField(QcRunStepField.DESCRIPTION, "the description of the step"))
                .setProject("theProject")
                .setDomain("theDomain")
                .commit();
    }
}
