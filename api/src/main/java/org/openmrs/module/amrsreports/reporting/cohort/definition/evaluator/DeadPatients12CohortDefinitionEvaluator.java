package org.openmrs.module.amrsreports.reporting.cohort.definition.evaluator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Cohort;
import org.openmrs.annotation.Handler;
import org.openmrs.api.context.Context;
import org.openmrs.module.amrsreports.reporting.cohort.definition.DeadPatients12CohortDefinition;
import org.openmrs.module.amrsreports.reporting.cohort.definition.DeadPatientsCohortDefinition;
import org.openmrs.module.reporting.cohort.EvaluatedCohort;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.cohort.definition.SqlCohortDefinition;
import org.openmrs.module.reporting.cohort.definition.evaluator.CohortDefinitionEvaluator;
import org.openmrs.module.reporting.cohort.definition.service.CohortDefinitionService;
import org.openmrs.module.reporting.evaluation.EvaluationContext;
import org.openmrs.module.reporting.evaluation.EvaluationException;

/**
 * Evaluator for Dead Patients Cohort Definition
 */
@Handler(supports = {DeadPatients12CohortDefinition.class})
public class DeadPatients12CohortDefinitionEvaluator implements CohortDefinitionEvaluator {


    private final Log log = LogFactory.getLog(this.getClass());

    @Override
    public EvaluatedCohort evaluate(CohortDefinition cohortDefinition, EvaluationContext context) throws EvaluationException {

        DeadPatients12CohortDefinition definition = (DeadPatients12CohortDefinition) cohortDefinition;

        if (definition == null)
            return null;

		context.addParameterValue("startDate", context.getParameterValue("startDate"));
        context.addParameterValue("endDate", context.getParameterValue("endDate"));

        String sql =
                "select person_id from person   " +
                        " where dead = 1   " +
                        " and death_date is not null   " +
                        " and death_date > (:startDate) and death_date <= date_add((:startDate),INTERVAL 12 MONTH)   " +
                        " union   " +
                        "    (select person_id from obs   " +
                        "     where concept_id = 1543   " +
                        "     and value_datetime is not null  " +
                        "     and value_datetime > (:startDate) and value_datetime <= date_add((:startDate),INTERVAL 12 MONTH)  " +
                        "    )";



        SqlCohortDefinition sqlCohortDefinition = new SqlCohortDefinition(sql);
        Cohort results = Context.getService(CohortDefinitionService.class).evaluate(sqlCohortDefinition, context);

        return new EvaluatedCohort(results, sqlCohortDefinition, context);
    }
}
