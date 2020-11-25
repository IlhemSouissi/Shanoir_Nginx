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

import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';

import { EntityService } from '../../../../shared/components/entity/entity.abstract.service';
import { ExtraData } from './extradata.model';
import * as PreclinicalUtils from '../../../utils/preclinical.utils';

@Injectable()
export class ExtraDataService extends EntityService<ExtraData>{
        
    API_URL = PreclinicalUtils.PRECLINICAL_API_EXAMINATION_URL;

    getEntityInstance() { return new ExtraData(); }         
    
   getExtraDatas(examId:number): Promise<ExtraData[]>{
        const url = `${PreclinicalUtils.PRECLINICAL_API_EXAMINATION_URL}/${examId}/${PreclinicalUtils.PRECLINICAL_EXTRA_DATA}${PreclinicalUtils.PRECLINICAL_ALL_URL}`;
        return this.http.get<ExtraData[]>(url)
            .map(entities => entities.map((entity) => this.toRealObject(entity)))
            .toPromise();
    }
  
    getExtraData(id:string): Promise<ExtraData>{
        return this.http.get<ExtraData>(PreclinicalUtils.PRECLINICAL_API_EXAMINATION_URL+"/"+id)
            .map((entity) => this.toRealObject(entity))
            .toPromise();
    }
  
    
    createExtraData(datatype:string,extradata: any): Observable<any> {
        const url = `${PreclinicalUtils.PRECLINICAL_API_EXAMINATION_URL}/${extradata.examination_id}/${datatype}`;
        return this.http
        .post<ExtraData>(url, JSON.stringify(extradata))
        .map(res => res);
    }
        
        
    postFile(fileToUpload: File,  extraData: ExtraData): Observable<any> {
        const endpoint = this.getUploadUrl(extraData);
        const formData: FormData = new FormData();
        formData.append('files', fileToUpload, fileToUpload.name);
        return this.http
            .post(endpoint, formData)
            .map(response => response);
    }
    	
    getUploadUrl(extraData: ExtraData): string {
        return `${PreclinicalUtils.PRECLINICAL_API_EXAMINATION_URL}/${PreclinicalUtils.PRECLINICAL_EXTRA_DATA}/upload/`+extraData.id;
    }

   	getDownloadUrl(extraData: ExtraData): string {
        return `${PreclinicalUtils.PRECLINICAL_API_EXAMINATION_URL}/${PreclinicalUtils.PRECLINICAL_EXTRA_DATA}/download/`+extraData.id;
    }
        
    
        
    deleteExtradata(extradata: ExtraData): Promise<void> {
        const url = `${PreclinicalUtils.PRECLINICAL_API_EXAMINATION_URL}/${extradata.examination_id}/${PreclinicalUtils.PRECLINICAL_EXTRA_DATA}/${extradata.id}`;
        return this.http.delete<void>(url)
            .toPromise();
    }
    
    download(extradata:ExtraData): Observable<any>{
        const url = `${PreclinicalUtils.PRECLINICAL_API_EXAMINATION_URL}/${extradata.examination_id}/${PreclinicalUtils.PRECLINICAL_EXTRA_DATA}/${extradata.id}/download`;
        return this.http.get<ExtraData>(url);
        //.map(res => res);
    }
    
        
    updateExtradata(datatype :string, id: number,extradata : ExtraData): Observable<ExtraData> {
        const url = `${PreclinicalUtils.PRECLINICAL_API_EXAMINATION_URL}/${extradata.examination_id}/`+datatype+`/`+id;
        return this.http
        .put<ExtraData>(url, JSON.stringify(extradata))
        .map(response => response);
    }
    
}