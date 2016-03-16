-- Add to BandDefinition report field

alter table REPORT_BAND_DEFINITION add column REPORT_ID uuid^

alter table REPORT_BAND_DEFINITION add constraint FK_REPORT_BAND_DEFINITION_TO_REPORT_REPORT
foreign key (REPORT_ID) references REPORT_REPORT (ID)^

-- Set reports for bands

create function updateBands(parentId uuid, reportId uuid) returns void as $$
declare
    bandId uuid;
    childBandId uuid;
begin
    for bandId in select ID from REPORT_BAND_DEFINITION where PARENT_DEFINITION_ID = parentId loop
      update REPORT_BAND_DEFINITION set REPORT_ID = reportId where ID = bandId;
      for childBandId in select ID from REPORT_BAND_DEFINITION where PARENT_DEFINITION_ID = bandId loop
        perform updateBands(bandId, reportId);
      end loop;
    end loop;
end;
$$ language plpgsql^

create function updateReports() returns void as $$
declare
    reportId uuid;
    rootBandId uuid;
begin
    for rootBandId in select ROOT_DEFINITION_ID from REPORT_REPORT loop
        select ID into reportId from REPORT_REPORT where ROOT_DEFINITION_ID = rootBandId;
        update REPORT_BAND_DEFINITION set REPORT_ID = reportId where ID = rootBandId;
        perform updateBands(rootBandId, reportId);
    end loop;
end;
$$ language plpgsql^

select updateReports()^

drop function updateReports()^

drop function updateBands(uuid,uuid)^