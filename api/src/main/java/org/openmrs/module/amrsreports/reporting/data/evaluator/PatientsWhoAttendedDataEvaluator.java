/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.amrsreports.reporting.data.evaluator;

import org.openmrs.Cohort;
import org.openmrs.annotation.Handler;
import org.openmrs.api.context.Context;
import org.openmrs.module.amrsreports.reporting.cohort.definition.AttendedPatientsCohortDefinition;
import org.openmrs.module.amrsreports.reporting.cohort.definition.BookedPatientsCohortDefinition;
import org.openmrs.module.amrsreports.reporting.cohort.definition.OnARTCohortDefinition;
import org.openmrs.module.amrsreports.reporting.data.AttendedStatusDataDefinition;
import org.openmrs.module.amrsreports.reporting.data.PatientOnARTDataDefinition;
import org.openmrs.module.reporting.cohort.definition.service.CohortDefinitionService;
import org.openmrs.module.reporting.data.person.EvaluatedPersonData;
import org.openmrs.module.reporting.data.person.definition.PersonDataDefinition;
import org.openmrs.module.reporting.data.person.evaluator.PersonDataEvaluator;
import org.openmrs.module.reporting.evaluation.EvaluationContext;
import org.openmrs.module.reporting.evaluation.EvaluationException;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;

import java.util.Date;
import java.util.Set;

/**
 * Evaluates an ObsForPersonDataDefinition to produce a PersonData
 */
@Handler(supports = AttendedStatusDataDefinition.class, order = 50)
public class PatientsWhoAttendedDataEvaluator implements PersonDataEvaluator {


	/**
	 * @should return the obs that match the passed definition configuration
	 * @see org.openmrs.module.reporting.data.person.evaluator.PersonDataEvaluator#evaluate(org.openmrs.module.reporting.data.person.definition.PersonDataDefinition, org.openmrs.module.reporting.evaluation.EvaluationContext)
	 */
	public EvaluatedPersonData evaluate(PersonDataDefinition definition, EvaluationContext context) throws EvaluationException {


        EvaluatedPersonData c = new EvaluatedPersonData(definition, context);

        if (context.getBaseCohort() != null && context.getBaseCohort().isEmpty()) {
            return c;
        }

        AttendedPatientsCohortDefinition attended = new AttendedPatientsCohortDefinition();

        //add params
        attended.addParameter(new Parameter("startDate", "Report Date", Date.class));
        attended.addParameter(new Parameter("endDate", "End Reporting Date", Date.class));

        context.addParameterValue("startDate", context.getParameterValue("startDate"));
        context.addParameterValue("endDate", context.getParameterValue("endDate"));

        Cohort patientsAttended = Context.getService(CohortDefinitionService.class).evaluate(attended, context);
        Set<Integer> patientsWhoAttended= patientsAttended.getMemberIds();

        for (Integer memberId : context.getBaseCohort().getMemberIds()) {
            String isTrue =  patientsWhoAttended.contains(memberId)?"Yes":"No";

            c.addData(memberId, isTrue);
        }

        return c;
	}

}
