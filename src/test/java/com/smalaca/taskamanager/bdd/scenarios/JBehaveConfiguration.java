package com.smalaca.taskamanager.bdd.scenarios;

import org.jbehave.core.Embeddable;
import org.jbehave.core.configuration.Configuration;
import org.jbehave.core.configuration.MostUsefulConfiguration;
import org.jbehave.core.embedder.Embedder;
import org.jbehave.core.failures.FailingUponPendingStep;
import org.jbehave.core.i18n.LocalizedKeywords;
import org.jbehave.core.io.CodeLocations;
import org.jbehave.core.io.LoadFromClasspath;
import org.jbehave.core.junit.JUnitStory;
import org.jbehave.core.model.ExamplesTableFactory;
import org.jbehave.core.model.TableParsers;
import org.jbehave.core.model.TableTransformers;
import org.jbehave.core.reporters.FilePrintStreamFactory.ResolveToPackagedName;
import org.jbehave.core.reporters.StoryReporterBuilder;
import org.jbehave.core.steps.InjectableStepsFactory;
import org.jbehave.core.steps.InstanceStepsFactory;
import org.jbehave.core.steps.ParameterControls;
import org.jbehave.core.steps.ParameterConverters;
import org.jbehave.core.steps.ParameterConverters.BooleanConverter;
import org.jbehave.core.steps.ParameterConverters.EnumConverter;
import org.jbehave.core.steps.ParameterConverters.ExamplesTableConverter;

import java.util.Properties;

import static java.util.Arrays.asList;
import static org.jbehave.core.reporters.Format.CONSOLE;
import static org.jbehave.core.reporters.Format.HTML;
import static org.jbehave.core.reporters.Format.TXT;

public abstract class JBehaveConfiguration extends JUnitStory {
    @Override
    public Embedder configuredEmbedder() {
        Embedder embedder = super.configuredEmbedder();
        embedder.useMetaFilters(asList("-skip"));

        embedder.embedderControls()
                .doFailOnStoryTimeout(true)
                .doGenerateViewAfterStories(true)
                .doIgnoreFailureInStories(false);

        return embedder;
    }

    @Override
    public Configuration configuration() {
        Class<? extends Embeddable> embeddableClass = this.getClass();
        ParameterConverters parameterConverters = parameterConverters(embeddableClass);

        return new MostUsefulConfiguration()
                .useParameterConverters(parameterConverters)
                .usePendingStepStrategy(new FailingUponPendingStep())
                .useStoryReporterBuilder(storyReporterBuilder(embeddableClass));
    }

    private StoryReporterBuilder storyReporterBuilder(Class<? extends Embeddable> embeddableClass) {
        return new StoryReporterBuilder()
                .withCodeLocation(CodeLocations.codeLocationFromClass(embeddableClass))
                .withDefaultFormats()
                .withPathResolver(new ResolveToPackagedName())
                .withViewResources(viewResources())
                .withFormats(CONSOLE, TXT, HTML)
                .withFailureTrace(true)
                .withFailureTraceCompression(true);
    }

    private ParameterConverters parameterConverters(Class<? extends Embeddable> embeddableClass) {
        ParameterConverters parameterConverters = new ParameterConverters();
        parameterConverters.addConverters(
                new ExamplesTableConverter(examplesTableFactory(embeddableClass, parameterConverters)),
                new EnumConverter(),
                new BooleanConverter("is", "is not"));

        return parameterConverters;
    }

    private ExamplesTableFactory examplesTableFactory(Class<? extends Embeddable> embeddableClass, ParameterConverters parameterConverters) {
        ExamplesTableFactory examplesTableFactory = new ExamplesTableFactory(
                new LocalizedKeywords(), new LoadFromClasspath(embeddableClass),
                parameterConverters, new ParameterControls(), new TableParsers(), new TableTransformers());
        return examplesTableFactory;
    }

    private Properties viewResources() {
        Properties viewResources = new Properties();
        viewResources.put("decorateNonHtml", "true");
        return viewResources;
    }

    @Override
    public InjectableStepsFactory stepsFactory() {
        return new InstanceStepsFactory(configuration(), this);
    }
}
