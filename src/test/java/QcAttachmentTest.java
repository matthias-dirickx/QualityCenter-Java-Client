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

import be.mdi.testing.qc.model.QcType;
import be.mdi.testing.qc.model.entities.QcAttachment;
import be.mdi.testing.qc.model.fields.QcAttachmentField;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import java.io.InputStream;
import java.io.StringWriter;

public class QcAttachmentTest {

    @Test
    public void qcAttachmentCreateObjectWithFieldsTest() {

        QcAttachment qcAttachment =
                new QcAttachment()
                    .setField(QcAttachmentField.DESCRIPTION, "a description with spaces and &é\"\'(§!èçà)-\')")
                    .setField(QcAttachmentField.NAME, "The Name");

        Assertions.assertEquals(
                qcAttachment.getField(QcAttachmentField.DESCRIPTION),
                "a description with spaces and &é\"\'(§!èçà)-\')");
        Assertions.assertEquals(
                qcAttachment.getField(QcAttachmentField.NAME),
                "The Name");
    }

    @Test
    public void qcAttachmentCreateObjectWithFieldsAndFileTest() throws java.io.IOException {

        QcAttachment qcAttachment =
                new QcAttachment()
                        .setField(QcAttachmentField.DESCRIPTION, "a description with spaces and &é\"\'(§!èçà)-\')")
                        .setField(QcAttachmentField.NAME, "The Name");

        qcAttachment.setAttachmentInputStream(this.getClass().getResourceAsStream("Atomium1.JPG"), "Atomium.JPG");

        InputStream testIs = qcAttachment.getAttachmentInputStream();

        Assertions.assertEquals(
                qcAttachment.getField(QcAttachmentField.DESCRIPTION),
                "a description with spaces and &é\"\'(§!èçà)-\')");
        Assertions.assertEquals(
                qcAttachment.getField(QcAttachmentField.NAME),
                "The Name");
        Assertions.assertEquals(
                this.getClass().getResourceAsStream("Atomium1.JPG").readAllBytes(),
                testIs.readAllBytes());
    }

    @Test
    public void qcAttachmenthasFileIndicatorIsTrueWhenFileAndFalseWhenNoFile() throws java.io.IOException {

        QcAttachment qcAttachment =
                new QcAttachment()
                        .setField(QcAttachmentField.DESCRIPTION, "a description with spaces and &é\"\'(§!èçà)-\')")
                        .setField(QcAttachmentField.NAME, "The Name");

        Assertions.assertEquals(
                qcAttachment.hasFile(),
                false
        );

        qcAttachment.setAttachmentInputStream(this.getClass().getResourceAsStream("Atomium1.JPG"), "Atomium.JPG");

        Assertions.assertEquals(
                qcAttachment.hasFile(),
                true);
    }

    @Test
    public void qcAttachmentFieldsToXmlTest() throws javax.xml.bind.JAXBException {
        QcAttachment qcAttachment =
                new QcAttachment()
                        .setField(QcAttachmentField.DESCRIPTION, "a description with spaces and &é\"\'(§!èçà)-\')")
                        .setField(QcAttachmentField.NAME, "The Name");
        qcAttachment.setAttachmentInputStream(this.getClass().getResourceAsStream("Atomium1.JPG"), "Atomium.JPG");

        Assertions.assertEquals(
                qcAttachment.getField(QcAttachmentField.DESCRIPTION),
                "a description with spaces and &é\"\'(§!èçà)-\')");
        Assertions.assertEquals(
                qcAttachment.getField(QcAttachmentField.NAME),
                "The Name");

        StringWriter sw = new StringWriter();

        JAXBContext contextObj = JAXBContext.newInstance(QcAttachment.class);
        Marshaller marshallerObj = contextObj.createMarshaller();
        marshallerObj.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshallerObj.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");

        marshallerObj.marshal(qcAttachment, sw);

        Assertions.assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                        "<Entity Type=\"attachment\">\n" +
                        "    <Fields>\n" +
                        "        <Field Name=\"name\">\n" +
                        "            <Value>The Name</Value>\n" +
                        "        </Field>\n" +
                        "        <Field Name=\"description\">\n" +
                        "            <Value>a description with spaces and &amp;é\"'(§!èçà)-')</Value>\n" +
                        "        </Field>\n" +
                        "    </Fields>\n" +
                        "</Entity>\n",
                sw.toString()
        );

        Assertions.assertEquals("Atomium.JPG", qcAttachment.getFileName());
    }

    @Test
    public void qcAttachmentWithOneParentLevelUrlCreation() {
        QcAttachment qcAttachment = new QcAttachment();
        qcAttachment.setProject("theProject");
        qcAttachment.setDomain("theDomain");

        qcAttachment.setParent(QcType.RUN, "3");

        Assertions.assertEquals(
                "rest/domains/theDomain/projects/theProject/runs/3/attachments",
                qcAttachment.getUrl());
    }

    @Test
    public void qcAttachmentWithTwoParentLevelsUrlCreation() {
        QcAttachment qcAttachment = new QcAttachment();
        qcAttachment.setProject("theProject");
        qcAttachment.setDomain("theDomain");

        qcAttachment.setParent(QcType.RUN_STEP, "1", QcType.RUN, "2");

        Assertions.assertEquals(
                "rest/domains/theDomain/projects/theProject/runs/2/run-steps/1/attachments",
                qcAttachment.getUrl()
        );
    }

    @Test
    public void qcAttachmentWithAnIdAndTwoParentLevelsUrlCreation() {
        QcAttachment qcAttachment = new QcAttachment();
        qcAttachment.setDomain("theDomain");
        qcAttachment.setProject("theProject");

        qcAttachment.setField(QcAttachmentField.ID, "3")
                .setParent(QcType.RUN_STEP, "1", QcType.RUN, "2");

        Assertions.assertEquals(
                "rest/domains/theDomain/projects/theProject/runs/2/run-steps/1/attachments/3",
                qcAttachment.getUrl()
        );
    }

    @Test
    public void qcAttachmentSendToQcEntityWithoutAnotherParentTest() {

    }

    @Test
    public void qcAttachmentAddMultipleAttachmentsTestWithoutSending() {

    }

    @Test
    public void qcAttachmentAddMultipleAttachmentsTestWithSending() {

    }
}
