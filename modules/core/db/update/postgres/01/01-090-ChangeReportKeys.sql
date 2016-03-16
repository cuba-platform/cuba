-- Description: reports now are hard deleted

alter table report_band_definition drop CONSTRAINT  fk_report_band_definition_to_report_band_definition^
alter table report_band_definition add CONSTRAINT fk_report_band_definition_to_report_band_definition FOREIGN KEY (parent_definition_id)
      REFERENCES report_band_definition (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE^

alter table report_report drop CONSTRAINT fk_report_report_to_report_band_definition^
alter table report_report add CONSTRAINT fk_report_report_to_report_band_definition FOREIGN KEY (root_definition_id)
      REFERENCES report_band_definition (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE^

ALTER table report_input_parameter drop CONSTRAINT fk_repor_input_parameter_to_report_report^
alter table report_input_parameter  add constraint fk_repor_input_parameter_to_report_report FOREIGN KEY (report_id)
      REFERENCES report_report (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE^

alter table report_data_set  drop constraint fk_report_data_set_to_report_band_definition^
alter table report_data_set  add constraint fk_report_data_set_to_report_band_definition FOREIGN KEY (band_definition)
      REFERENCES report_band_definition (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE^

alter table REPORT_REPORTS_ROLES drop constraint FK_REPORT_REPORTS_ROLES_TO_REPORT^
alter table REPORT_REPORTS_ROLES add constraint FK_REPORT_REPORTS_ROLES_TO_REPORT
foreign key (REPORT_ID) references REPORT_REPORT(ID) on delete cascade^

alter table report_report_screen drop CONSTRAINT fk_report_report_screen_to_report_report^
alter table report_report_screen add CONSTRAINT fk_report_report_screen_to_report_report FOREIGN KEY (report_id)
      REFERENCES report_report (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE cascade^