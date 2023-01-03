if not EXISTS(SELECT 1 FROM sysobjects WHERE sysobjects.name = 'Log')
create table Log (
Id int identity,
Data datetime,
RequestCountry nvarchar(50),
RequestGenre nvarchar(50),
ResponseArtist nvarchar(255),
ResponseSong nvarchar(255),
ResponseLyrics nvarchar(max)
)
GO


if exists (select 1 from sysobjects where name = 'usp_WriteLog')
drop procedure usp_WriteLog
go

create procedure usp_WriteLog
  @piRequestCountry nvarchar(50) = null,
	@piRequestGenre nvarchar(50) = null,
	@piResponseArtist nvarchar(255) = null,
	@piResponseSong nvarchar(255) = null,
	@piResponseLyrics nvarchar(max) = null
as

declare @vData datetime

select @vData = getdate() 

insert into Log(Data, RequestCountry, RequestGenre, ResponseArtist, ResponseSong, ResponseLyrics)
values (@vData, @piRequestCountry, @piRequestGenre, @piResponseArtist, @piResponseSong, @piResponseLyrics)

return
go


if exists (select 1 from sysobjects where name = 'usp_GetData')
drop procedure usp_GetData
go

create procedure usp_GetData
  @piRequestCountry nvarchar(50) = null,
	@piRequestGenre nvarchar(50) = null,
	@poArtist nvarchar(255) = null output,
	@poSong nvarchar(255) = null output,
	@poLyrics nvarchar(max) = null output,
	@poValue int = null output
as

SET NOCOUNT ON

declare @vLogData datetime, @vId int

select 
	@vLogData = max(data),
	@vId = Id
from 
	Log 
where RequestCountry = @piRequestCountry 
	and RequestGenre = @piRequestGenre
group by Id

if datediff(mi, @vLogData, getdate()) <= 10
begin
	select 
		@poArtist = ResponseArtist,
		@poSong = ResponseSong,
		@poLyrics = ResponseLyrics
	from Log
	where Id = @vId
	
	set @poValue = 1
end
else 
begin
	select 
		@poArtist = 'N/A',
		@poSong = 'N/A',
		@poLyrics = 'N/A'
		
	if @poArtist = 'N/A' or @poSong = 'N/A' or @poLyrics = 'N/A'
		set @poValue = 0
end

return
go
