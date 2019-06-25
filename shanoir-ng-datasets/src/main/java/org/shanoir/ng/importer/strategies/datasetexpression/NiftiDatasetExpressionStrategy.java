/**
 * Shanoir NG - Import, manage and share neuroimaging data
 * Copyright (C) 2009-2019 Inria - https://www.inria.fr/
 * Contact us on https://project.inria.fr/shanoir/
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see https://www.gnu.org/licenses/gpl-3.0.html
 */

package org.shanoir.ng.importer.strategies.datasetexpression;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;

import org.shanoir.ng.dataset.model.DatasetExpression;
import org.shanoir.ng.dataset.model.DatasetExpressionFormat;
import org.shanoir.ng.datasetfile.DatasetFile;
import org.shanoir.ng.importer.dto.ExpressionFormat;
import org.shanoir.ng.importer.dto.ImportJob;
import org.shanoir.ng.importer.dto.Serie;
import org.shanoir.ng.processing.DatasetProcessingType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class NiftiDatasetExpressionStrategy implements DatasetExpressionStrategy {

	private static final String SUB_PREFIX = "sub-";
	
	private static final String SES_PREFIX = "ses-";
	
	private static final String ANAT = "anat";
	
	/** Logger. */
	private static final Logger LOG = LoggerFactory.getLogger(NiftiDatasetExpressionStrategy.class);
	
	@Value("${datasets-data}")
	private String niftiStorageDir;
	
	@Override
	public DatasetExpression generateDatasetExpression(Serie serie, ImportJob importJob,
			ExpressionFormat expressionFormat) {
		
		DatasetExpression niftiDatasetExpression = new DatasetExpression();
		niftiDatasetExpression.setCreationDate(LocalDateTime.now());
		niftiDatasetExpression.setDatasetExpressionFormat(DatasetExpressionFormat.NIFTI_SINGLE_FILE);
		niftiDatasetExpression.setDatasetProcessingType(DatasetProcessingType.FORMAT_CONVERSION);
		
		//TODO ATO Specify nifti converter used..
		niftiDatasetExpression.setNiftiConverterId(4L);
				
		niftiDatasetExpression.setOriginalNiftiConversion(true);
		if (serie.getIsMultiFrame()) {
			niftiDatasetExpression.setMultiFrame(true);
			niftiDatasetExpression.setFrameCount(new Integer(serie.getMultiFrameCount()));
		}
		
		if (expressionFormat != null & expressionFormat.getType().equals("nii")) {

			final String subLabel = SUB_PREFIX + importJob.getPatients().get(0).getSubject().getName();
			// TODO BIDS: Remove ses level if only one examination, add ses level if new examination imported for the same subject
			final String sesLabel = SES_PREFIX + importJob.getExaminationId();
			// TODO BIDS: Get data type (anat, func, dwi, fmap, meg and beh) from MrDatasetNature and/or ExploredEntity
			final String dataTypeLabel = ANAT + "/";
			
			final File outDir = new File(niftiStorageDir + File.separator + subLabel + File.separator + sesLabel + File.separator + dataTypeLabel + File.separator);
			outDir.mkdirs();
			
			for (org.shanoir.ng.importer.dto.DatasetFile datasetFile : expressionFormat.getDatasetFiles()) {
				
				File srcFile = new File(datasetFile.getPath().replace("file:" , ""));
				String originalNiftiName = srcFile.getAbsolutePath().substring(datasetFile.getPath().lastIndexOf('/') + 1);
				File destFile = new File(outDir.getAbsolutePath() + File.separator + originalNiftiName);
				Path niftiFinalLocation = null;
				try {
					niftiFinalLocation = Files.copy(srcFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
				} catch (IOException e) {
					LOG.error("IOException generating nifti Dataset Expression", e);
				}

				DatasetFile niftiDatasetFile = new DatasetFile();
				niftiDatasetFile.setPacs(false);
				niftiDatasetFile.setPath(niftiFinalLocation.toUri().toString());
				niftiDatasetExpression.getDatasetFiles().add(niftiDatasetFile);
				niftiDatasetFile.setDatasetExpression(niftiDatasetExpression);
			}
		}
		
		
//		final DatasetExpression datasetExpressionNifti = createDatasetExpression(mrDataset, datasetExpressionToPacs,
//				null);
//		datasetExpressionNifti.setDatasetExpressionFormat(DatasetExpressionFormat.NIFTI_SINGLE_FILE);
//		datasetExpressionNifti.setDatasetProcessingType(DatasetProcessingType.FORMAT_CONVERSION);
//		datasetExpressionNifti.setNiftiConverterId(serie.getNiftiConverterId()));
//		
//		/* Set the Nifti Dataset expression as the "original nifti conversion" */
//		datasetExpressionNifti.setOriginalNiftiConversion(true);
//
//		/* Get the value of the dcm2nii version setted during conversion */
//		IConvertToNifti niftiConverter = (IConvertToNifti) Component.getInstance("niftiConverter");
//		if (niftiConverter != null) {
//			/* Have to set converter to clidcm if files have been converted using clidcm */
//			boolean is4D = false;
//			/*
//			 * Load the clidcm converter because the converter can switch from dcm2nii to
//			 * clidcm in case of 4d volume sequence
//			 */
//			INiftiConverterHome niftiConverterHome = (INiftiConverterHome) Component.getInstance("niftiConverterHome");
//			NiftiConverter clidcmConverter = niftiConverterHome.findById(3L);
//
//			if (isConvertAs4D(index))
//				is4D = true;
//			if (niftiConverter.getNiftiConverter() != null) {
//				if (is4D && niftiConverter.getNiftiConverter().isClidcm(true) && clidcmConverter != null) {
//					datasetExpressionNifti.setNiftiConverter(clidcmConverter);
//				} else {
//					datasetExpressionNifti.setNiftiConverter(niftiConverter.getNiftiConverter());
//				}
//
//			}
//			if (niftiConverter.getNiftiConverterVersion() != null
//					&& !("").equals(niftiConverter.getNiftiConverterVersion())) {
//				if (is4D && niftiConverter.getNiftiConverter().isClidcm(true) && clidcmConverter != null) {
//					datasetExpressionNifti.setNiftiConverterVersion(clidcmConverter.getName());
//				} else {
//					datasetExpressionNifti.setNiftiConverterVersion(niftiConverter.getNiftiConverterVersion());
//				}
//			}
//		}
//
//		for (final File niftiFile : niftiFiles) {
//			log.debug("createMrDataset : processing the file " + niftiFile.getName());
//			final DatasetFile niftiDatasetFile = new DatasetFile();
//			niftiDatasetFile.setPath(niftiFile.toURI().toString().replaceAll(" ", "%20"));
//			datasetExpressionNifti.getDatasetFileList().add(niftiDatasetFile);
//			niftiDatasetFile.setDatasetExpression(datasetExpressionNifti);
//		}
//
//		// if necessary, rename the bvec and bval files (for DTI)
//		renameBvecBval(datasetExpressionNifti);
//
//		/*
//		 * if there was some DTI images, then we can now use the bvec and bval files to
//		 * create the diffusion gradients and add them to the MR Protocol. Indeed, it is
//		 * more likely to do so now because extracting the diffusion gradients from the
//		 * dicom files is tricky.
//		 */
//		extractDiffusionGradients(mrDatasetAcquisition.getMrProtocol(), mrDataset, datasetExpressionNifti);
		return niftiDatasetExpression;
	}

//	/**
//	 * Check for the given property if the dicom value matches the value in the
//	 * configuration file.
//	 *
//	 * @param serieNumber
//	 *            the serie number
//	 * @param property
//	 *            the property
//	 *
//	 * @return true, if check dicom from properties
//	 */
//	private boolean checkDicomFromProperties(final int serieNumber, final String property) {
//		boolean result = false;
//		final String[] seriesDescriptionForDiffusion = ShanoirConfig.getPropertyAsArray(property);
//		for (final String item : seriesDescriptionForDiffusion) {
//			int tag = Tag.toTag(item.split("==")[0]);
//			String value = item.split("==")[1];
//			log.debug("checkDicomFromProperties : tag=" + tag + ", value=" + value);
//			final String dicomValue = getString(serieNumber, tag);
//			String wildcard = ShanoirUtil.wildcardToRegex(value);
//			if (dicomValue != null && dicomValue.matches(wildcard)) {
//				result = true;
//				break;
//			}
//		}
//		log.debug("checkDicomFromProperties : End, return " + result);
//		return result;
//	}
//	
//	/**
//	 * Return the value of the given property key as an array of Strings. The value in the property file must be separated by a comma.
//	 * @param key
//	 *            key
//	 * @return
//	 */
//	public static String[] getPropertyAsArray(final String key) {
//		String property = getProperty(key);
//		String[] result = null;
//		if (property != null) {
//			result = property.split(";");
//		}
//		return result;
//	}
//	
//	/*
//	 * (non-Javadoc)
//	 *
//	 * @see
//	 * org.shanoir.dicom.extractor.IMetadataExtractor#isConvertAs4D
//	 * (int)
//	 */
//	public boolean isConvertAs4D(final int serieNumber) {
//		return checkDicomFromProperties(serieNumber, "convertAs4D");
//	}

}
