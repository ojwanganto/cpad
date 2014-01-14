package org.openmrs.module.amrsreports.reporting.data.evaluator;

import org.openmrs.annotation.Handler;
import org.openmrs.api.context.Context;
import org.openmrs.module.amrsreports.reporting.data.INHStartDateDataDefinition;
import org.openmrs.module.amrsreports.reporting.data.LTFUTODeadDataDefinition;
import org.openmrs.module.amrsreports.service.MohCoreService;
import org.openmrs.module.amrsreports.util.MOHReportUtil;
import org.openmrs.module.reporting.data.person.EvaluatedPersonData;
import org.openmrs.module.reporting.data.person.definition.PersonDataDefinition;
import org.openmrs.module.reporting.data.person.evaluator.PersonDataEvaluator;
import org.openmrs.module.reporting.evaluation.EvaluationContext;
import org.openmrs.module.reporting.evaluation.EvaluationException;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Handler INH start month/year column
 */
@Handler(supports = INHStartDateDataDefinition.class, order = 50)
public class INHStartDateDataEvaluator implements PersonDataEvaluator {

	@Override
	public EvaluatedPersonData evaluate(PersonDataDefinition definition, EvaluationContext context) throws EvaluationException {
		EvaluatedPersonData ret = new EvaluatedPersonData(definition, context);

		if (context.getBaseCohort().isEmpty())
			return ret;

		Map<String, Object> m = new HashMap<String, Object>();
		m.put("patientIds", context.getBaseCohort());


		String obsINHSQL = "select person_id, min(obs_datetime) " +
                "from obs " +
                "where voided=0 and (concept_id in(1270,1193,1110,1637,1111,6903,1264,1113) and value_coded=656)" +
                "group by person_id";

		Map<Integer, Date> inhObs = makeDateMapFromSQL(obsINHSQL, m);


		for (Integer personId : context.getBaseCohort().getMemberIds()) {

               ret.addData(personId,inhObs.get(personId));
		}

		return ret;
	}

	private Map<Integer, Date> makeDateMapFromSQL(String sql, Map<String, Object> substitutions) {
		List<Object> data = Context.getService(MohCoreService.class).executeSqlQuery(sql, substitutions);
		return makeDateMap(data);
	}

	/**
	 * generates a map of integers to dates, assuming this is the kind of response expected from the SQL
	 */
	private Map<Integer, Date> makeDateMap(List<Object> data) {
		Map<Integer, Date> m = new HashMap<Integer, Date>();
		for (Object o : data) {
			Object[] parts = (Object[]) o;
			if (parts.length == 2) {
				Integer pId = (Integer) parts[0];
				Date date = (Date) parts[1];
				m.put(pId, date);
			}
		}

		return m;
	}

}
