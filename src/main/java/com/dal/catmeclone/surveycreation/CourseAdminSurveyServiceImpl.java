package com.dal.catmeclone.surveycreation;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.dal.catmeclone.AbstractFactory;
import com.dal.catmeclone.SystemConfig;
import com.dal.catmeclone.exceptionhandler.UserDefinedSQLException;
import com.dal.catmeclone.model.Course;
import com.dal.catmeclone.model.ModelAbstractFactory;
import com.dal.catmeclone.model.Survey;
import com.dal.catmeclone.model.SurveyQuestion;

public class CourseAdminSurveyServiceImpl implements CourseAdminSurveyService {

	private Logger LOGGER = Logger.getLogger(CourseAdminSurveyController.class.getName());

	AbstractFactory abstractFactory = SystemConfig.instance().getAbstractFactory();
	SurveyCreationAbstractFactory surveyCreationAbstractFactory = abstractFactory.createSurveyCreationAbstractFactory();
	ModelAbstractFactory modelAbstractFactory = abstractFactory.createModelAbstractFactory();

	@Override
	public Survey getSurveyDetailsForCourse(Course course) throws UserDefinedSQLException {

		CourseAdminSurveyDao courseAdminSurveyDao = surveyCreationAbstractFactory.createSurveyCreationDao();
		LOGGER.info("Calling Dao Methods to retrieve the details form database");
		Survey survey = courseAdminSurveyDao.getSurveyDetailsForCourse(course);
		if (survey == null) {
			LOGGER.info("No Survey Exists in database for the course");
			/* If survey doesn't exist in database for the given course, create a dummy
			   course to pass it controller, which pass it to view as new survey.*/
			survey = new Survey(0, course, new ArrayList<SurveyQuestion>(), false, 2);
		}
		return survey;
	}

	@Override
	public boolean saveSurvey(Survey survey) throws UserDefinedSQLException {

		CourseAdminSurveyDao courseAdminSurveyDao = surveyCreationAbstractFactory.createSurveyCreationDao();
		List<SurveyQuestion> listOfQuestionsToBeRemoved = new ArrayList<SurveyQuestion>();
		if (survey.getSurveyId() == 0) {
			// Calling DAO to insert new survey into DB
			LOGGER.info("Surevy identifies as a new survey. Calling Dao to insert survey into database");
			courseAdminSurveyDao.createSurveyDetails(survey);
		} else {
			// Calling DAO to update existing survey into DB
			LOGGER.info("Survey identifies as a existing survey. Identifying the updatese");
			// getting existing Survey Question for the survey
			Survey existingsSurvey = courseAdminSurveyDao.getSurveyDetailsForCourse(survey.getCourse());
			List<SurveyQuestion> listOfExistingSurveyQuestion = existingsSurvey.getSurveyQuestions();
			listOfQuestionsToBeRemoved = listOfExistingSurveyQuestion;
			LOGGER.info("Segregating the survey questions which has to be update and which needs to be removed");
			// Identifying and segregating the survey questions which has to be update and which needs to be removed.
			listOfExistingSurveyQuestion.removeAll(survey.getSurveyQuestions());
			LOGGER.info("Calling DAO to update survey details into database");
			courseAdminSurveyDao.updateSurveyDetails(survey, listOfQuestionsToBeRemoved);
		}
		return true;
	}

	@Override
	public boolean publishSurvey(int surveyId) throws UserDefinedSQLException {

		CourseAdminSurveyDao courseAdminSurveyDao = surveyCreationAbstractFactory.createSurveyCreationDao();
		// Calling DAO method to publish the survey
		return courseAdminSurveyDao.publishSurvey(surveyId);
	}

}