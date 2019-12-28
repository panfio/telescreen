package ru.panfio.telescreen.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import ru.panfio.telescreen.model.Autotimer;
import ru.panfio.telescreen.repository.AutotimerRepository;

import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mock;
import static ru.panfio.telescreen.service.TestFiles.toInputStream;

public class AutoTimerServiceTest {

    private AutoTimerService service;
    private ObjectStorage objectStorage;
    private AutotimerRepository autotimerRepository;

    @Before
    public void setUp() {
        autotimerRepository = mock(AutotimerRepository.class);
        objectStorage = mock(ObjectStorage.class);
        service = new AutoTimerService(autotimerRepository, objectStorage);
    }

    @Test
    public void autotimerRecordsShouldBeCreatedCorrectly() throws FileNotFoundException {
        List<String> activityFiles = Arrays.asList("autotimer/activities-20191127-101619.json");
        when(objectStorage.listAllObjects()).thenReturn(activityFiles);
        when(objectStorage.getInputStream(activityFiles.get(0))).thenReturn(toInputStream(TestFiles.ACTIVITIES));

        service.processAutotimerRecords();

        @SuppressWarnings("unchecked") final ArgumentCaptor<List<Autotimer>> argument = ArgumentCaptor.forClass(List.class);
        verify(autotimerRepository).saveAll(argument.capture());
        List<Autotimer> list = argument.getValue();

        assertEquals(2, list.size());
        assertNull(list.get(0).getId());
        assertThat(list.get(0).getName(), is("Google Chrome -> Dreams by Ytho."));
        assertThat(list.get(0).getStartTime(), is(LocalDateTime.of(2019, 11, 26, 23, 52, 1)));
        assertThat(list.get(0).getEndTime(), is(LocalDateTime.of(2019, 11, 26, 23, 52, 10)));
        assertThat(list.get(0).getType(), is(1));
    }

}
