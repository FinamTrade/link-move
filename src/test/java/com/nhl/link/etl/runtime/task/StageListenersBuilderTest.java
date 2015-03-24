package com.nhl.link.etl.runtime.task;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;

import com.nhl.link.etl.Execution;
import com.nhl.link.etl.annotation.AfterSourceRowsConverted;
import com.nhl.link.etl.annotation.AfterSourcesMapped;
import com.nhl.link.etl.annotation.AfterTargetsMatched;
import com.nhl.link.etl.annotation.AfterTargetsMerged;
import com.nhl.link.etl.runtime.task.createorupdate.CreateOrUpdateSegment;
import com.nhl.link.etl.runtime.task.createorupdate.MockCreateOrUpdateListener;

public class StageListenersBuilderTest {

	private ListenersBuilder listenersBuilder;

	@Before
	public void before() {
		listenersBuilder = new ListenersBuilder(AfterSourceRowsConverted.class, AfterSourcesMapped.class,
				AfterTargetsMatched.class, AfterTargetsMerged.class);
	}

	@Test
	public void testAddListener() {

		MockCreateOrUpdateListener listener1 = new MockCreateOrUpdateListener();
		listenersBuilder.addListener(listener1);

		assertEquals(4, listenersBuilder.getListeners().size());
		assertEquals(2, listenersBuilder.getListeners().get(AfterTargetsMatched.class).size());
		assertEquals(1, listenersBuilder.getListeners().get(AfterSourceRowsConverted.class).size());
		assertEquals(1, listenersBuilder.getListeners().get(AfterSourcesMapped.class).size());
		assertEquals(1, listenersBuilder.getListeners().get(AfterTargetsMerged.class).size());

		verify(listener1.getMockDelegate(), times(0)).afterSourceRowsConverted(Matchers.any(Execution.class),
				Matchers.any(CreateOrUpdateSegment.class));
		listenersBuilder.getListeners().get(AfterSourceRowsConverted.class).get(0)
				.afterStageFinished(mock(Execution.class), mock(CreateOrUpdateSegment.class));
		verify(listener1.getMockDelegate()).afterSourceRowsConverted(Matchers.any(Execution.class),
				Matchers.any(CreateOrUpdateSegment.class));

		verify(listener1.getMockDelegate(), times(0)).afterTargetMatched(Matchers.any(Execution.class),
				Matchers.any(CreateOrUpdateSegment.class));
		verify(listener1.getMockDelegate(), times(0)).afterTargetMatched2(Matchers.any(Execution.class),
				Matchers.any(CreateOrUpdateSegment.class));
		listenersBuilder.getListeners().get(AfterTargetsMatched.class).get(0)
				.afterStageFinished(mock(Execution.class), mock(CreateOrUpdateSegment.class));
		listenersBuilder.getListeners().get(AfterTargetsMatched.class).get(1)
				.afterStageFinished(mock(Execution.class), mock(CreateOrUpdateSegment.class));
		verify(listener1.getMockDelegate()).afterTargetMatched(Matchers.any(Execution.class),
				Matchers.any(CreateOrUpdateSegment.class));
		verify(listener1.getMockDelegate()).afterTargetMatched2(Matchers.any(Execution.class),
				Matchers.any(CreateOrUpdateSegment.class));

	}
}
