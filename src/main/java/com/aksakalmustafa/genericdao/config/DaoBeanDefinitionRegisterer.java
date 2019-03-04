package com.aksakalmustafa.genericdao.config;

import java.util.Set;

import javax.persistence.Entity;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.ResolvableType;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import com.aksakalmustafa.genericdao.GenericDaoApplication;
import com.aksakalmustafa.genericdao.dao.BaseDaoImpl;
import com.aksakalmustafa.genericdao.model.BaseEntity;

@Configuration
public class DaoBeanDefinitionRegisterer implements BeanDefinitionRegistryPostProcessor {

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory bf) throws BeansException {
		DefaultListableBeanFactory factory = (DefaultListableBeanFactory) bf;

		ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
		scanner.addIncludeFilter(new AnnotationTypeFilter(Entity.class));
		Set<BeanDefinition> components = scanner
				.findCandidateComponents(GenericDaoApplication.class.getPackage().getName());
		for (BeanDefinition beanDefinition : components) {
			Class<?> genericType = getClass(beanDefinition.getBeanClassName());

			if (!genericType.getSuperclass().equals(BaseEntity.class)) {
				continue;
			}

			ResolvableType resolvableType = ResolvableType.forClassWithGenerics(BaseDaoImpl.class, genericType);
			String[] registeredBeans = factory.getBeanNamesForType(resolvableType);
			if (registeredBeans == null || registeredBeans.length == 0) {
				String beanName = genericType.getSimpleName().substring(0, 1).toUpperCase()
						+ genericType.getSimpleName().substring(1) + "Dao";

				RootBeanDefinition rootBean = new RootBeanDefinition();
				rootBean.setTargetType(resolvableType);
				rootBean.setLazyInit(true);
				rootBean.setFactoryBeanName(beanName);
				rootBean.setBeanClass(resolvableType.getRawClass());
				rootBean.setScope(ConfigurableBeanFactory.SCOPE_SINGLETON);
				rootBean.getConstructorArgumentValues()
						.addIndexedArgumentValue(0, resolvableType.getGeneric(0).getRawClass());
				factory.registerBeanDefinition(beanName, rootBean);
			}
		}
	}

	private Class<?> getClass(String className) throws BeansException {
		if (className != null) {
			try {
				return Class.forName(className);
			} catch (ClassNotFoundException e) {
				throw new BeanInitializationException("beanClass not found", e);
			}
		}
		return null;
	}

	@Override
	public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
	}

}
